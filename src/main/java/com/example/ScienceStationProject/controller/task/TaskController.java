package com.example.ScienceStationProject.controller.task;

import com.example.ScienceStationProject.controller.task.dto.FormFieldData;
import com.example.ScienceStationProject.controller.task.dto.OptionDTO;
import com.example.ScienceStationProject.controller.task.dto.PropertyDTO;
import com.example.ScienceStationProject.controller.task.dto.TaskInfoDTO;
import com.example.ScienceStationProject.controller.task.dto.processInfo.ProcessInfoDTO;
import com.example.ScienceStationProject.controller.task.dto.processInfo.ReviewCommentDTO;
import com.example.ScienceStationProject.model.Magazine;
import com.example.ScienceStationProject.model.ScienceBranch;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.magazine.MagazineService;
import com.example.ScienceStationProject.service.scienceBranch.ScienceBranchService;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.rest.mapper.MultipartFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private UserService userService;

    private MagazineService magazineService;

    private RuntimeService runtimeService;

    private TaskService taskService;

    private FormService formService;

    private ScienceBranchService scienceBranchService;
    @Autowired
    public TaskController(UserService userService, RuntimeService runtimeService, TaskService taskService, FormService formService,ScienceBranchService scienceBranchService,
                          MagazineService magazineService) {
        this.userService = userService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.formService = formService;
        this.scienceBranchService = scienceBranchService;
        this.magazineService = magazineService;
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<?> getTasksForUser(){
        User u = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(u.getId().toString()).list();

        List<TaskInfoDTO> taskInfoDTOList = new ArrayList<>();

        for(Task t : tasks){
            TaskInfoDTO taskInfoDTO = new TaskInfoDTO();
            taskInfoDTO.setMagazineId(((Long)runtimeService.getVariable(t.getProcessInstanceId(),"magazineId")).toString());
            taskInfoDTO.setMagazineName((String)runtimeService.getVariable(t.getProcessInstanceId(),"magazineName"));
            taskInfoDTO.setTaskId(t.getId());
            taskInfoDTO.setTaskName(t.getName());
            taskInfoDTOList.add(taskInfoDTO);
        }

        return ResponseEntity.ok().body(taskInfoDTOList);

    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProcessInfo(@PathVariable("id") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInfoDTO processInfoDTO = new ProcessInfoDTO();
        Long authorId = Long.valueOf((String)runtimeService.getVariable(task.getProcessInstanceId(),"authorId"));
        User u = userService.findById(authorId);
        processInfoDTO.setAuthorName(u.getFirstName() + " " + u.getLastName());
        processInfoDTO.setJournalAbstract((String)runtimeService.getVariable(task.getProcessInstanceId(),"journalAbstract"));
        processInfoDTO.setKeyPoints((String)runtimeService.getVariable(task.getProcessInstanceId(),"keyPoints"));
        processInfoDTO.setHeadline((String)runtimeService.getVariable(task.getProcessInstanceId(),"headline"));
        if(runtimeService.getVariable(task.getProcessInstanceId(),"branch") != null) {
            Long branchId = Long.valueOf((String) runtimeService.getVariable(task.getProcessInstanceId(), "branch"));
            processInfoDTO.setBranchName(scienceBranchService.findById(branchId).getName());
        } else
            processInfoDTO.setBranchName(null);
        processInfoDTO.setReviewCommentDTOList((ArrayList<ReviewCommentDTO>)runtimeService.getVariable(task.getProcessInstanceId(),"reviews"));
        int x = 3;
        return ResponseEntity.ok(processInfoDTO);
    }

    @RequestMapping(value = "/task/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getTaskFormData(@PathVariable("id") String id){
        TaskFormData taskFormData = formService.getTaskFormData(id);
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        List<FormField> fields = taskFormData.getFormFields();
        List<FormFieldData> formFieldDataList = new ArrayList<>();

        for(FormField f: fields){
            FormFieldData formFieldData = new FormFieldData();
            formFieldData.setKey(f.getId());
            formFieldData.setLabel(f.getLabel());
            formFieldData.setRequired(true);
            formFieldData.setValue(null);
            /*if(f.getValue().getValue() == null)
                formFieldData.setValue(null);
            else
                formFieldData.setValue(f.getValue().getValue().toString());*/

            /*popisivanje svih select naredbi*/
            if(f.getId().equals("branch")){
                for(ScienceBranch b: scienceBranchService.findAll()) {
                    int x = 3;
                    OptionDTO optionDTO = new OptionDTO();
                    optionDTO.setKey(b.getId().toString());
                    optionDTO.setValue(b.getName());
                    formFieldData.getOptions().add(optionDTO);
                }
            }
            else if(f.getId().equals("mainEditorDecision")){
                OptionDTO optionDTO1 = new OptionDTO();
                optionDTO1.setKey("1");
                optionDTO1.setValue("Poslati rad nazad autoru na ispravku");

                OptionDTO optionDTO2 = new OptionDTO();
                optionDTO2.setKey("2");
                optionDTO2.setValue("Odbiti rad");

                OptionDTO optionDTO3 = new OptionDTO();
                optionDTO3.setKey("3");
                optionDTO3.setValue("Poslati rad na dalju recenziju");

                formFieldData.getOptions().add(optionDTO1);
                formFieldData.getOptions().add(optionDTO2);
                formFieldData.getOptions().add(optionDTO3);
            }
            else if(f.getId().equals("reviewers")){
                ArrayList<String> availableReviewers = (ArrayList<String>)runtimeService.getVariable(task.getProcessInstanceId(),"availableReviewers");
                for(String reviewer : availableReviewers){
                    User u = userService.findById(Long.valueOf(reviewer));
                    OptionDTO optionDTO = new OptionDTO();
                    optionDTO.setKey(u.getId().toString());
                    optionDTO.setValue(u.getFirstName() + " " + u.getLastName());
                    formFieldData.getOptions().add(optionDTO);
                }
            }
            else if(f.getId().equals("editorDecision")){
                OptionDTO optionDTO1 = new OptionDTO();
                optionDTO1.setKey("1");
                optionDTO1.setValue("Potrebna veca dorada");

                OptionDTO optionDTO2 = new OptionDTO();
                optionDTO2.setKey("2");
                optionDTO2.setValue("Potrebna manja dorada");

                OptionDTO optionDTO3 = new OptionDTO();
                optionDTO3.setKey("3");
                optionDTO3.setValue("Prihvatiti rad");

                OptionDTO optionDTO4 = new OptionDTO();
                optionDTO4.setKey("4");
                optionDTO4.setValue("Odbijanje rada");

                formFieldData.getOptions().add(optionDTO1);
                formFieldData.getOptions().add(optionDTO2);
                formFieldData.getOptions().add(optionDTO3);
                formFieldData.getOptions().add(optionDTO4);
            }
            else
            {
                formFieldData.setOptions(null);
            }
            formFieldDataList.add(formFieldData);

            if(f.getProperties().isEmpty())
                formFieldData.setProperties(null);
            else{
                for(Map.Entry<String,String> entry: f.getProperties().entrySet()){
                    PropertyDTO propertyDTO = new PropertyDTO();
                    propertyDTO.setFieldName(entry.getKey());
                    propertyDTO.setFieldType(entry.getValue());
                    formFieldData.getProperties().add(propertyDTO);
                }
            }

        }

        return ResponseEntity.ok().body(formFieldDataList);
    }

    @RequestMapping(value="/uploadPdf/{id}",method = RequestMethod.POST)
    public ResponseEntity<?> executeMultipartTask(@RequestParam MultipartFile pdf, @PathVariable("id") String taskId){

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String absolutePath = System.getProperty("user.dir") + "\\src\\main\\resources\\uploads";

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        String fileName = StringUtils.cleanPath(pdf.getOriginalFilename());
        File convertFile = new File(absolutePath+"\\"+task.getProcessInstanceId()+".pdf");

        if(pdf.isEmpty() || fileName.contains(".."))
            return ResponseEntity.badRequest().build();

        try {
            convertFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(convertFile);
            fout.write(pdf.getBytes());
            fout.close();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/execute/{id}",method = RequestMethod.POST)
    public ResponseEntity<?> executeTask(@RequestBody Map<String,Object> form, @PathVariable("id") String taskId){

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if(task.getName().equals("Recenzija rada")){
            return executeTaskReviewers(form,task);
        }
        if(!task.getAssignee().equals(user.getId().toString()))
            return ResponseEntity.badRequest().build();

        runtimeService.setVariables(task.getProcessInstanceId(),form);
        taskService.complete(taskId);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> executeTaskReviewers(Map<String,Object> form, Task task){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        form.put("reviewer",user.getFirstName() + " " +user.getLastName());
        if(!task.getAssignee().equals(user.getId().toString()))
            return ResponseEntity.badRequest().build();
        List<Map<String,Object>> reviews = (List<Map<String,Object>>) runtimeService.getVariable(task.getProcessInstanceId(),"reviews");
        if(reviews == null)
            reviews = new ArrayList<>();

        reviews.add(form);
        runtimeService.setVariable(task.getProcessInstanceId(),"reviews",reviews);
        taskService.complete(task.getId());
        return ResponseEntity.ok().build();

    }
    @RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadTaskFile (@PathVariable("id") String taskId, HttpServletResponse response){
        String absolutePath = System.getProperty("user.dir") + "\\src\\main\\resources\\uploads";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        absolutePath+="\\"+task.getProcessInstanceId()+".pdf";

        File pdf = new File(absolutePath);
        Path path = Paths.get(absolutePath);
        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity.ok().contentLength(pdf.length())
                    .contentType(MediaType.parseMediaType("application/pdf")).body(resource);
        } catch (IOException e){
            return ResponseEntity.status(404).build();
        }

    }
    /*@RequestMapping(value = "/reviewerTask/{id}",method = RequestMethod.POST)
    public ResponseEntity<?> executeTaskReviewers(@RequestBody Map<String,Object> form, @PathVariable("id") String taskId){

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if(!task.getAssignee().equals(user.getId().toString()))
            return ResponseEntity.badRequest().build();

        List<Map<String,Object>> reviews = (List<Map<String,Object>>) runtimeService.getVariable(task.getProcessInstanceId(),"reviews");
        if(reviews == null)
            reviews = new ArrayList<>();

        reviews.add(form);
        runtimeService.setVariable(task.getProcessInstanceId(),"reviews",reviews);
        taskService.complete(taskId);
        return ResponseEntity.ok().build();
    }*/
}
