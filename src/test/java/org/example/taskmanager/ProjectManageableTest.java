package org.example.taskmanager;

class ProjectManageableTest extends ManageableContractTest {

    @Override
    protected Manageable create() {
        return new Project("TestProject", "desc");
    }
}
