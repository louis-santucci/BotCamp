package com.botcamp.botcamp_api.controller;

import com.botcamp.botcamp_api.config.BotcampUser;
import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.execution.ExecutionStatus;
import com.botcamp.botcamp_api.execution.ExecutionType;
import com.botcamp.botcamp_api.service.TaskExecutionService;
import com.botcamp.common.request.SortingOrderParameter;
import com.botcamp.common.response.GenericResponse;
import com.botcamp.common.utils.HttpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.botcamp.common.config.SwaggerConfig.BEARER_AUTHENTICATION;
import static com.botcamp.common.endpoints.BotcampApiEndpoint.*;

@RestController
@CrossOrigin
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@RequestMapping(path = API_EXECUTION)
@Tag(name = EXECUTION_CONTROLLER)
public class ExecutionController {
    private static final String BEGIN_DATE_QUERY_PARAM = "begin_date";
    private static final String END_DATE_QUERY_PARAM = "end_date";
    private static final String STATUS_QUERY_PARAM = "status";
    private static final String TYPE_QUERY_PARAM = "type";
    private static final String SORTING_ORDER_QUERY_PARAM = "sort";
    private static final String USER_ONLY_QUERY_PARAM = "user_only";

    private final TaskExecutionService taskExecutionService;

    public ExecutionController(TaskExecutionService taskExecutionService) {
        this.taskExecutionService = taskExecutionService;
    }

    @GetMapping(EXECUTION_GET)
    @Operation(summary = "Gets all executions in function of the different provided")
    public ResponseEntity<GenericResponse<List<Execution>>> getExecutions(@RequestParam(value = BEGIN_DATE_QUERY_PARAM, required = false) String beginDate,
                                                                          @RequestParam(value = END_DATE_QUERY_PARAM, required = false) String endDate,
                                                                          @RequestParam(value = STATUS_QUERY_PARAM, required = false) List<ExecutionStatus> statuses,
                                                                          @RequestParam(value = TYPE_QUERY_PARAM, required = false) List<ExecutionType> types,
                                                                          @RequestParam(value = SORTING_ORDER_QUERY_PARAM, required = false) SortingOrderParameter updateTimeSorting,
                                                                          @RequestParam(value = USER_ONLY_QUERY_PARAM) boolean userOnly,
                                                                          Authentication authentication) {
        try {
            BotcampUser user = (BotcampUser) authentication.getPrincipal();
            List<Execution> executionList = taskExecutionService.getExecutions(beginDate, endDate, statuses, types, updateTimeSorting, user, userOnly);
            return HttpUtils.generateResponse(HttpStatus.OK, HttpUtils.SUCCESS, executionList);
        } catch (Exception e) {
            return HttpUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
