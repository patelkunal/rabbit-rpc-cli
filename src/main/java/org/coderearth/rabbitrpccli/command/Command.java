package org.coderearth.rabbitrpccli.command;

import java.util.Properties;

/**
 * Created by kunal_patel on 20/11/17.
 */
@lombok.Builder
@lombok.Getter

public class Command {

    @lombok.NonNull
    private String exchange;

    @lombok.NonNull
    private String queue;

    @lombok.NonNull
    private String operation;

    private Properties headers;

    // private Object body;

}
