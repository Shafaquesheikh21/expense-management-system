package com.expenseledger.expense_ledger.service;
import com.expenseledger.expense_ledger.dto.AddMemberRequest;
import com.expenseledger.expense_ledger.dto.CreateGroupRequest;
import com.expenseledger.expense_ledger.entity.Group;
import com.expenseledger.expense_ledger.entity.GroupMember;
import com.expenseledger.expense_ledger.entity.User;
import com.expenseledger.expense_ledger.repository.GroupMemberRepository;
import com.expenseledger.expense_ledger.repository.GroupRepository;
import com.expenseledger.expense_ledger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuditLogService auditLogService;
    public Group createGroup(CreateGroupRequest request, String creatorEmail){
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));
        Group group = new Group();
        group.setName(request.getName());
        group.setCreated_By(creator);
        Group savedGroup = groupRepository.save(group);

        GroupMember creatorMembership = new GroupMember();
        creatorMembership.setGroup(savedGroup);
        creatorMembership.setUser(creator);
        groupMemberRepository.save(creatorMembership);
        auditLogService.logAction(savedGroup, creator, "GROUP_CREATED", "Group'" + savedGroup.getName() + "'created");
        return  savedGroup;
    }
    public GroupMember addMember(Long groupId, AddMemberRequest request){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email"));
        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);
        GroupMember savedMember = groupMemberRepository.save(member);
        auditLogService.logAction(group, user, "MEMBER_ADDED",
                user.getName() + " was added to the group");
        return savedMember;
    }
    public List<GroupMember> getGroupMembers(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId);
    }
    public List<Group> getUserGroups(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<GroupMember> memberships = groupMemberRepository.findByUserId(user.getId());
        return memberships.stream()
                .map(GroupMember::getGroup)
                .collect(java.util.stream.Collectors.toList());
    }
}
