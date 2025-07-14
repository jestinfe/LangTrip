package kr.co.sist.e_learning.admin.log;

import java.util.List;

public interface AdminLogService {
    public void addLog(AdminLogDTO logDTO);
    public List<AdminLogDTO> getAllLogs();
}
