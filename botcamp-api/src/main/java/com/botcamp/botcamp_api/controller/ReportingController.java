package com.botcamp.botcamp_api.controller;

import com.botcamp.botcamp_api.config.BotcampUser;
import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.execution.ExecutionType;
import com.botcamp.botcamp_api.service.EmailSender;
import com.botcamp.botcamp_api.service.TaskExecutionService;
import com.botcamp.botcamp_api.service.impl.TokenRefresherImpl;
import com.botcamp.common.mail.QueryParameter;
import com.botcamp.common.response.GenericResponse;
import com.botcamp.common.utils.HttpUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.botcamp.common.config.SwaggerConfig.BEARER_AUTHENTICATION;
import static com.botcamp.common.endpoints.BotcampApiEndpoint.*;

@RestController
@CrossOrigin
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@RequestMapping(path = API_REPORTING)
@Tag(name = REPORTING_CONTROLLER)
public class ReportingController {

    private final EmailSender emailSender;
    private final TaskExecutionService taskExecutionService;

    public ReportingController(EmailSender emailSender,
                               TokenRefresherImpl tokenRefresher,
                               TaskExecutionService taskExecutionService) {
        this.emailSender = emailSender;
        this.taskExecutionService = taskExecutionService;
    }

    @PostMapping(path = SCHEDULE)
    public ResponseEntity<GenericResponse<Execution>> scheduleJob(@RequestParam(value = FROM_QUERY_PARAM, required = false) String from,
                                                                  @RequestParam(value = BEGIN_DATE_QUERY_PARAM, required = false) String beginDate,
                                                                  @RequestParam(value = END_DATE_QUERY_PARAM, required = false) String endDate,
                                                                  @RequestParam(value = SUBJECT_QUERY_PARAM, required = false) String subject,
                                                                  Authentication authentication) {
        BotcampUser user = (BotcampUser) authentication.getPrincipal();
        try {
            QueryParameter queryParameter = new QueryParameter(from, beginDate, endDate, subject);
            Execution execution = new Execution(taskExecutionService.createExecutionEntity(queryParameter, ExecutionType.GMAIL, user));
            return HttpUtils.generateResponse(HttpStatus.OK, HttpUtils.SUCCESS, execution);
        } catch (Exception e) {
            return HttpUtils.generateResponse(HttpStatus.OK, e.getMessage(), null);
        }
    }
}
