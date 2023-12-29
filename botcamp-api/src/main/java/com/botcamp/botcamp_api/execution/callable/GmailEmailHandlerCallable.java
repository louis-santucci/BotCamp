package com.botcamp.botcamp_api.execution.callable;

import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.execution.callable.exporter.CallableExporter;
import com.botcamp.botcamp_api.service.GmailGatewayClient;
import com.botcamp.common.mail.EmailResults;
import com.botcamp.common.mail.QueryParameter;

import java.util.List;

public class GmailEmailHandlerCallable extends BotcampCallable<Void> {

    private final GmailGatewayClient gatewayClient;
    private final List<CallableExporter> exporters;

    public GmailEmailHandlerCallable(Execution execution,
                                     GmailGatewayClient gatewayClient,
                                     List<CallableExporter> exporters) {
        super(execution);
        this.gatewayClient = gatewayClient;
        this.exporters = exporters;
    }

    @Override
    public Void call() throws Exception {
        Execution execution = getExecution();
        QueryParameter queryParameter = execution.getQueryParameter();
        EmailResults results = gatewayClient.getEmails(queryParameter.getBeginDate(), queryParameter.getEndDate(), queryParameter.getFrom(), queryParameter.getSubject(), true);

        for (CallableExporter exporter: exporters) {
            exporter.export(execution, results);
        }

        return null;
    }
}
