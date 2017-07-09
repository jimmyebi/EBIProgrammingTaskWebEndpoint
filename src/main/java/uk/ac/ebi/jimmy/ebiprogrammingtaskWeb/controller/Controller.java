package uk.ac.ebi.jimmy.ebiprogrammingtaskWeb.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.jimmy.ebiprogrammingtaskWeb.logic.Processor;
import uk.ac.ebi.jimmy.ebiprogrammingtaskWeb.resource.Message;

@RestController
@RequestMapping("/EBIProgrammingTaskWebEndpoint")
public class Controller {
    
    protected Processor processorService;


    @Autowired
    public Controller(Processor processorService) {
        this.processorService = processorService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> processEndpoint(@RequestBody Message message) {
        
        Message resultMessage = new Message(null);
        BeanUtils.copyProperties(message, resultMessage);     
        processorService.readStringInput(message.getMessage());     
        processorService.startProcess();
        resultMessage.setMessage(processorService.getStorage().getRangedResult());
        return resultMessage != null ? new ResponseEntity<>(resultMessage, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}