package com.esb.plugin.fixture;

import com.esb.internal.commons.FileUtils;

import java.net.URL;

public class Json {

    private static final String FIXTURE_BASE_PATH = "/com/esb/plugin/fixture/flow/";

    interface DataProvider {

        String path();

        default String json() {
            URL url = Json.class.getResource(FIXTURE_BASE_PATH + path());
            return FileUtils.readFrom(url);
        }
    }

    public enum CompleteFlow implements DataProvider {
        Sample() {
            @Override
            public String path() {
                return "complete_flow_sample.json";
            }
        },

        NestedRouter() {
            @Override
            public String path() {
                return "complete_flow_with_nested_router.json";
            }
        },

        NestedFork() {
            @Override
            public String path() {
                return "complete_flow_with_nested_fork.json";
            }
        },

        NestedEmptyFork() {
            @Override
            public String path() {
                return "complete_flow_with_empty_fork.json";
            }
        },

        NodesBetweenScopes() {
            @Override
            public String path() {
                return "complete_flow_with_nodes_between_scopes.json";
            }
        }
    }

    public enum GenericComponent implements DataProvider {
        Sample() {
            @Override
            public String path() {
                return "generic_component_sample.json";
            }
        },

        WithTypeObject {
            @Override
            public String path() {
                return "generic_component_with_type_object.json";
            }
        },

        WithTypeObjectReference {
            @Override
            public String path() {
                return "generic_component_with_type_object_reference.json";
            }
        }
    }

    public enum Fork implements DataProvider {
        Sample() {
            @Override
            public String path() {
                return "fork_sample.json";
            }
        },

        WithoutSuccessorInsideScope() {
            @Override
            public String path() {
                return "fork_without_successor_inside_scope.json";
            }
        }
    }

    public enum Router implements DataProvider {
        Sample() {
            @Override
            public String path() {
                return "router_sample.json";
            }
        }
    }

    public enum FlowReference implements DataProvider {
        Sample() {
            @Override
            public String path() {
                return "flow_reference_sample.json";
            }
        }
    }

    public enum Configuration implements DataProvider {
        Sample() {
            @Override
            public String path() {
                return "configuration_sample.json";
            }
        };
    }
}
