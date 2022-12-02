package com.nineplus.bestwork.runner;

import com.nineplus.bestwork.repository.PermissionRepository;
import com.nineplus.bestwork.repository.RoleRepository;
import com.nineplus.bestwork.repository.SysActionRepository;
import com.nineplus.bestwork.repository.SysMonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationDataRunner implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SysMonitorRepository monitorRepository;

    @Autowired
    private SysActionRepository sysActionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.insertSystemDataRole();
        }
        if (monitorRepository.count() == 0) {
            monitorRepository.insertSystemDataMonitor();
        }
        if (sysActionRepository.count() == 0) {
            sysActionRepository.insertSystemDataAction();
        }
        if (permissionRepository.count() == 0) {
            permissionRepository.insertSystemPermissionData();
        }

    }
}
