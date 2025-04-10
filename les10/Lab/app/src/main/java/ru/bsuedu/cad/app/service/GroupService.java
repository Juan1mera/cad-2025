package ru.bsuedu.cad.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.bsuedu.cad.app.entity.Group;
import ru.bsuedu.cad.app.repository.GroupRepository;
import ru.bsuedu.cad.app.repository.StudentRepository;

@Service
public class GroupService {
        final private StudentRepository studentRepository;
    final private GroupRepository groupRepository;

    public GroupService(StudentRepository studentRepository,  GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }


    public List<Group> searchGroupByPartName(String text) {
        return  groupRepository.searchByDescription(text);
    }
}
