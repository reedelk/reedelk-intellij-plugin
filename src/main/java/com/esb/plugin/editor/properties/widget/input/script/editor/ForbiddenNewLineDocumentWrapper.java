package com.esb.plugin.editor.properties.widget.input.script.editor;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.editor.ex.EditReadOnlyListener;
import com.intellij.openapi.editor.ex.LineIterator;
import com.intellij.openapi.editor.ex.RangeMarkerEx;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolderEx;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.util.List;

public class ForbiddenNewLineDocumentWrapper implements DocumentEx, UserDataHolderEx {

    private final DocumentEx wrapped;

    ForbiddenNewLineDocumentWrapper(DocumentEx wrapped) {
        this.wrapped = wrapped;
    }

    @NotNull
    @Override
    public String getText() {
        return wrapped.getText();
    }

    @NotNull
    @Override
    public String getText(@NotNull TextRange range) {
        return wrapped.getText(range);
    }

    @NotNull
    @Override
    public CharSequence getCharsSequence() {
        return wrapped.getCharsSequence();
    }

    @NotNull
    @Override
    public CharSequence getImmutableCharSequence() {
        return wrapped.getImmutableCharSequence();
    }

    @NotNull
    @Override
    public char[] getChars() {
        return wrapped.getChars();
    }

    @Override
    public int getTextLength() {
        return wrapped.getTextLength();
    }

    @Override
    public int getLineCount() {
        return wrapped.getLineCount();
    }

    @Override
    public int getLineNumber(int offset) {
        return wrapped.getLineNumber(offset);
    }

    @Override
    public int getLineStartOffset(int line) {
        return wrapped.getLineStartOffset(line);
    }

    @Override
    public int getLineEndOffset(int line) {
        return wrapped.getLineEndOffset(line);
    }

    @Override
    public boolean isLineModified(int line) {
        return wrapped.isLineModified(line);
    }

    @Override
    public void insertString(int offset, @NotNull CharSequence s) {
        String replaced = s.toString().replaceAll("\\n", "");
        wrapped.insertString(offset, replaced);
    }

    @Override
    public void deleteString(int startOffset, int endOffset) {
        wrapped.deleteString(startOffset, endOffset);
    }

    @Override
    public void replaceString(int startOffset, int endOffset, @NotNull CharSequence s) {
        String replaced = s.toString().replaceAll("\\n", "");
        wrapped.replaceString(startOffset, endOffset, replaced);
    }

    @Override
    public boolean isWritable() {
        return wrapped.isWritable();
    }

    @Override
    public long getModificationStamp() {
        return wrapped.getModificationStamp();
    }

    @Override
    public void fireReadOnlyModificationAttempt() {
        wrapped.fireReadOnlyModificationAttempt();
    }

    @Override
    public void addDocumentListener(@NotNull DocumentListener listener) {
        wrapped.addDocumentListener(listener);
    }

    @Override
    public void addDocumentListener(@NotNull DocumentListener listener, @NotNull Disposable parentDisposable) {
        wrapped.addDocumentListener(listener, parentDisposable);
    }

    @Override
    public void removeDocumentListener(@NotNull DocumentListener listener) {
        wrapped.removeDocumentListener(listener);
    }

    @NotNull
    @Override
    public RangeMarker createRangeMarker(int startOffset, int endOffset) {
        return wrapped.createRangeMarker(startOffset, endOffset);
    }

    @NotNull
    @Override
    public RangeMarker createRangeMarker(int startOffset, int endOffset, boolean surviveOnExternalChange) {
        return wrapped.createRangeMarker(startOffset, endOffset, surviveOnExternalChange);
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        wrapped.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        wrapped.removePropertyChangeListener(listener);
    }

    @Override
    public void setReadOnly(boolean isReadOnly) {
        wrapped.setReadOnly(isReadOnly);
    }

    @NotNull
    @Override
    public RangeMarker createGuardedBlock(int startOffset, int endOffset) {
        return wrapped.createGuardedBlock(startOffset, endOffset);
    }

    @Override
    public void removeGuardedBlock(@NotNull RangeMarker block) {
        wrapped.removeGuardedBlock(block);
    }

    @Nullable
    @Override
    public RangeMarker getOffsetGuard(int offset) {
        return wrapped.getOffsetGuard(offset);
    }

    @Nullable
    @Override
    public RangeMarker getRangeGuard(int start, int end) {
        return wrapped.getRangeGuard(start, end);
    }

    @Override
    public void startGuardedBlockChecking() {
        wrapped.startGuardedBlockChecking();
    }

    @Override
    public void stopGuardedBlockChecking() {
        wrapped.stopGuardedBlockChecking();
    }

    @Override
    public void setCyclicBufferSize(int bufferSize) {
        wrapped.setCyclicBufferSize(bufferSize);
    }

    @Override
    public void setText(@NotNull CharSequence text) {
        wrapped.setText(text);
    }

    @NotNull
    @Override
    public RangeMarker createRangeMarker(@NotNull TextRange textRange) {
        return wrapped.createRangeMarker(textRange);
    }

    @Override
    public int getLineSeparatorLength(int line) {
        return wrapped.getLineSeparatorLength(line);
    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return wrapped.getUserData(key);
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
        wrapped.putUserData(key, value);
    }

    @NotNull
    @Override
    public LineIterator createLineIterator() {
        return wrapped.createLineIterator();
    }

    @Override
    public void setModificationStamp(long modificationStamp) {
        wrapped.setModificationStamp(modificationStamp);
    }

    @Override
    public void replaceText(@NotNull CharSequence chars, long newModificationStamp) {
        String replaced = chars.toString().replaceAll("\\n", "");
        wrapped.replaceText(replaced, newModificationStamp);
    }

    @Override
    public void moveText(int srcStart, int srcEnd, int dstOffset) {
        wrapped.moveText(srcStart, srcEnd, dstOffset);
    }

    @Override
    public boolean removeRangeMarker(@NotNull RangeMarkerEx rangeMarker) {
        return wrapped.removeRangeMarker(rangeMarker);
    }

    @Override
    public void registerRangeMarker(@NotNull RangeMarkerEx rangeMarker, int start, int end, boolean greedyToLeft, boolean greedyToRight, int layer) {
        wrapped.registerRangeMarker(rangeMarker, start, end, greedyToLeft, greedyToRight, layer);
    }

    @Override
    public boolean processRangeMarkers(@NotNull Processor<? super RangeMarker> processor) {
        return wrapped.processRangeMarkers(processor);
    }

    @Override
    public boolean processRangeMarkersOverlappingWith(int start, int end, @NotNull Processor<? super RangeMarker> processor) {
        return wrapped.processRangeMarkersOverlappingWith(start, end, processor);
    }

    @NotNull
    @Override
    public <T> T putUserDataIfAbsent(@NotNull Key<T> key, @NotNull T value) {
        return ((UserDataHolderEx) wrapped).putUserDataIfAbsent(key, value);
    }

    @Override
    public <T> boolean replace(@NotNull Key<T> key, @Nullable T oldValue, @Nullable T newValue) {
        return ((UserDataHolderEx) wrapped).replace(key, oldValue, newValue);
    }

    @Override
    public void setStripTrailingSpacesEnabled(boolean isEnabled) {
        wrapped.setStripTrailingSpacesEnabled(isEnabled);
    }

    @Override
    public void addEditReadOnlyListener(@NotNull EditReadOnlyListener listener) {
        wrapped.addEditReadOnlyListener(listener);
    }

    @Override
    public void removeEditReadOnlyListener(@NotNull EditReadOnlyListener listener) {
        wrapped.removeEditReadOnlyListener(listener);
    }

    @Override
    public void suppressGuardedExceptions() {
        wrapped.suppressGuardedExceptions();
    }

    @Override
    public void unSuppressGuardedExceptions() {
        wrapped.unSuppressGuardedExceptions();
    }

    @Override
    public boolean isInEventsHandling() {
        return wrapped.isInEventsHandling();
    }

    @Override
    public void clearLineModificationFlags() {
        wrapped.clearLineModificationFlags();
    }

    @Override
    public boolean isInBulkUpdate() {
        return wrapped.isInBulkUpdate();
    }

    @Override
    public void setInBulkUpdate(boolean value) {
        wrapped.setInBulkUpdate(value);
    }

    @NotNull
    @Override
    public List<RangeMarker> getGuardedBlocks() {
        return wrapped.getGuardedBlocks();
    }

    @Override
    public int getModificationSequence() {
        return wrapped.getModificationSequence();
    }

}
