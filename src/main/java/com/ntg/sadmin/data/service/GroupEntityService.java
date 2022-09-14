package com.ntg.sadmin.data.service;

import com.ntg.sadmin.TenantContext;
import com.ntg.sadmin.data.entities.Group;
import com.ntg.sadmin.data.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupEntityService {

    @Autowired
    private GroupRepository groupRepository;

    public Group findByName(String groupName) {
        return groupRepository.findByName(groupName, TenantContext.getCompanyName());
    }

    //@addedBy:Aya.Ramadan=>Dev-00002237 :Not able to add organization/group with the same name of organization/group in different company"
    public Group findByNameAndParentId(String groupName, Long parentGroupId) {
        if (parentGroupId != null && parentGroupId > 0) {
            return groupRepository.findByNameAndParentId(groupName, parentGroupId, TenantContext.getCompanyName());
        } else {
            return groupRepository.findByNameAndParentIdIsNull(groupName, TenantContext.getCompanyName());
        }
    }

    public Group findOne(Long parentGroupId) {
        Optional<Group> group = groupRepository.findById(parentGroupId);
        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }


    public Group save(Group group) {
        return groupRepository.save(group);
    }

    public List<Group> findAll() {
        return groupRepository.findAll(TenantContext.getCompanyName());
    }

    public List<Group> findAllGroupsByIds(List<Long> groupsIds) {
        return groupRepository.findAllGroupsByIds(groupsIds, TenantContext.getCompanyName());
    }

    public List<Group> findAll(String companyName) {
        //get company name from request instead of context
        return groupRepository.findAll(companyName);
    }
}
