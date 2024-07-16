package com.vargens.api_todo_list.controller.dto.Task;

import java.util.List;

public record ReponseTaskDTO(List<TaskItemDTO> taskItems, int page, int pageSize, int totalPages, Long totalElements) {

}
