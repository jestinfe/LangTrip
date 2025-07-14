package kr.co.sist.e_learning.admin.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminLogServiceImpl implements AdminLogService {

    @Autowired
    private AdminLogDAO adminLogDAO;

    @Override
    public void addLog(AdminLogDTO logDTO) {
        adminLogDAO.insertLog(logDTO);
    }

    @Override
    public List<AdminLogDTO> getAllLogs() {
        return adminLogDAO.selectAllLogs();
    }
}
