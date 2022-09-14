package com.ntg.sadmin.data.repositories;

import com.ntg.sadmin.data.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Transactional
    @Query(value = "SELECT * FROM sa_group  WHERE Name = :groupName and company_name = :companyName", nativeQuery = true)
    Group findByName(@Param("groupName") String groupName, @Param("companyName") String companyName);

    //@addedBy:Aya.Ramadan=>Dev-00002237 :Not able to add organization/group with the same name of organization/group in different company"
    @Transactional
    @Query(value = "SELECT * FROM sa_group  WHERE Name = :groupName and PARENT_GROUP_ID = :parentGroupId and company_name = :companyName", nativeQuery = true)
    Group findByNameAndParentId(@Param("groupName") String groupName, @Param("parentGroupId") Long parentGroupId, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT s.recid from sa_group s where s.parent_group_id IN (:recId) and s.company_name = :companyName", nativeQuery = true)
    List<Long> getAllOrganizationsOfEachCompanyByRecId(@Param("recId") List<Long> recId, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT s.recid from sa_group s where s.parent_group_id IN (:organizationsList) and s.company_name = :companyName", nativeQuery = true)
    List<Long> getAllGroupsOfEachOrganization(@Param("organizationsList") List<Long> organizationsList, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "SELECT g.groupid from sa_user_group g where g.groupid IN (:groupsList)", nativeQuery = true)
    List<Long> checkIfMembersInThisGroup(@Param("groupsList") List<Long> groupsList);

    @Transactional
    @Query(value = "SELECT s.gruop_type from sa_group s where s.recid = :groupId and s.company_name = :companyName", nativeQuery = true)
    Long getGroupTypeId(@Param("groupId") Long groupId, @Param("companyName") String companyName);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sa_group s where s.recid IN (:groupsList) and s.company_name = :companyName", nativeQuery = true)
    void deleteGroupsByRecId(@Param("groupsList") List<Long> groupsList, @Param("companyName") String companyName);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sa_group s where s.recid IN (:organizationsList) and s.company_name = :companyName ", nativeQuery = true)
    void deleteOrganizationsByRecId(@Param("organizationsList") List<Long> organizationsList, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "select *  FROM sa_group where company_name = :companyName ", nativeQuery = true)
    List<Group> findAll(@Param("companyName") String companyName);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sa_group where company_name = :companyName ", nativeQuery = true)
    void deleteAll(@Param("companyName") String companyName);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sa_user_group g where g.groupid IN (:groupsList)", nativeQuery = true)
    void deleteFromGroup(@Param("groupsList") List<Long> groupsList);

    @Transactional
    @Query(value = "SELECT * FROM sa_group  WHERE Name = :groupName and PARENT_GROUP_ID is null and company_name = :companyName", nativeQuery = true)
    Group findByNameAndParentIdIsNull(@Param("groupName") String groupName, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "select *  FROM sa_group where recid in (:groupsIds) and company_name = :companyName ", nativeQuery = true)
    List<Group> findAllGroupsByIds(@Param("groupsIds") List<Long> groupsIds, @Param("companyName") String companyName);

    @Transactional
    @Query(value = "select count(1) from sa_user_group where groupid = :groupId and userid = :userId", nativeQuery = true)
    Long checkIfUserExistsInGroup(@Param("groupId") Long groupId, @Param("userId") Long userId);

}
