package com.ibeetl.admin.core.file;

import java.io.OutputStream;
import java.util.List;
/**
 * 文件持久化，預設為文件係統，可以擴展到fastfds等
 * @author xiandafu
 *
 */
public interface FileService {
	/**
	 * 得到一個臨時文件操作
	 * @param name
	 * @return
	 */
	public FileItem createFileTemp(String name);
	
	/**
	 * 創建一個持久的文檔
	 * @param name
	 * @param bizType
	 * @param bizId
	 * @param userId
	 * @param orgId
	 * @param tags
	 * @return
	 */
	 public FileItem createFileItem(String name,String bizType,String bizId,Long userId,Long orgId,String fileBatchId,List<FileTag> tags);

	
	public FileItem loadFileItemByPath(String path);
	public FileItem getFileItemById(Long id);
	public FileItem getFileItemById(Long id,String batchFileId);
	
	public List<FileItem> queryByUserId(Long userId,List<FileTag> tags);
	public List<FileItem> queryByBiz(String bizType,String bizId); 
	public List<FileItem> queryByBatchId(String fileBatchId); 
	/**
	 * 刪除某個文件
	 * @param id
	 * @param fileBatchId，用於驗證
	 */
	public void removeFile(Long id,String fileBatchId);
	
	/**
	 * 完善附件信息
	 * @param fileBatchId
	 * @param bizType
	 * @param bizId
	 */
	public void updateFile(String fileBatchId,String bizType,String bizId);
	
	

    
	
}
