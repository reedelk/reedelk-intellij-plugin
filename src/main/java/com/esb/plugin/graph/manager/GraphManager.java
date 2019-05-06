package com.esb.plugin.graph.manager;

import com.esb.internal.commons.FileUtils;
import com.esb.plugin.graph.FlowGraph;
import com.esb.plugin.graph.GraphSnapshot;
import com.esb.plugin.graph.SnapshotListener;
import com.esb.plugin.graph.deserializer.GraphDeserializer;
import com.esb.plugin.graph.serializer.GraphSerializer;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.AncestorListenerAdapter;
import com.intellij.util.ThrowableRunnable;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.AncestorEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static java.util.Arrays.stream;

/**
 * Centralizes updates of the graph coming from the following sources:
 * - The text editor associated with the flow designer (the user manually updates the JSON)
 * - Designer updates:
 * - drag and drop and moving around components
 * - Properties updates:
 * - component's property changed
 */
public class GraphManager extends AncestorListenerAdapter implements FileEditorManagerListener, DocumentListener, SnapshotListener {

    private static final Logger LOG = Logger.getInstance(GraphManager.class);

    private final Module module;
    private final Project project;
    private final VirtualFile graphFile;
    private final GraphSnapshot snapshot;

    private String graphAsJson;

    private Document document;

    public GraphManager(@NotNull Project project, @NotNull Module module, @NotNull VirtualFile graphFile, @NotNull GraphSnapshot snapshot) {
        this.module = module;
        this.project = project;
        this.snapshot = snapshot;
        this.graphFile = graphFile;
        this.snapshot.addListener(this);

        MessageBusConnection busConnection = project.getMessageBus().connect();
        busConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this);

        try {
            graphAsJson = FileUtils.readFrom(new URL(graphFile.getUrl()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        if (file.equals(graphFile)) {
            findRelatedEditorDocument(source, file)
                    .ifPresent(document -> {
                        this.document = document;
                        this.document.addDocumentListener(GraphManager.this);
                    });
        }
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        if (file.equals(graphFile)) {
            this.document.removeDocumentListener(this);
            this.document = null;
        }
    }

    /**
     * The JSON representing the graph was manually changed by the user.
     * The graph must be re-built.
     *
     * @param event the change object triggering this callback.
     */
    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        Document document = event.getDocument();
        this.graphAsJson = document.getText();
    }

    @Override
    public void onDataChange(@NotNull FlowGraph graph) {
        String json = GraphSerializer.serialize(graph);
        writeOnFile(json);
    }

    @Override
    public void onStructureChange(@NotNull FlowGraph graph) {
        String json = GraphSerializer.serialize(graph);
        writeOnFile(json);
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        if (graphAsJson != null) {
            GraphDeserializer.deserialize(module, graphAsJson)
                    .ifPresent(updatedGraph -> snapshot.updateSnapshot(this, updatedGraph));
        }
    }

    private void writeOnFile(String json) {
        try {
            WriteCommandAction.writeCommandAction(project)
                    .run((ThrowableRunnable<Throwable>) () -> document.setText(json));
        } catch (Throwable throwable) {
            LOG.error("Could not write Graph's JSON data", throwable);
        }
    }

    /**
     * Finds the Document Object this Editor's GraphManager is referring to.
     */
    private Optional<Document> findRelatedEditorDocument(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        FileEditor[] editors = source.getEditors(file);
        return stream(editors).filter(fileEditor -> fileEditor instanceof TextEditor)
                .findFirst()
                .map(fileEditor -> (TextEditor) fileEditor)
                .map(textEditor -> textEditor.getEditor().getDocument());
    }
}
