package com.ibeetl.admin.console.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellReference;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibeetl.admin.console.dao.DictConsoleDao;
import com.ibeetl.admin.console.web.dto.DictExcelImportData;
import com.ibeetl.admin.core.entity.CoreDict;
import com.ibeetl.admin.core.entity.CoreUser;
import com.ibeetl.admin.core.service.CoreBaseService;
import com.ibeetl.admin.core.util.ExcelError;
import com.ibeetl.admin.core.util.PlatformException;

/**
 * CoreDict Service
 */

@Service
@Transactional
public class DictConsoleService extends CoreBaseService<CoreDict> {

    @Autowired private DictConsoleDao dictDao;

    public PageQuery<CoreDict>queryByCondition(PageQuery query){
        PageQuery ret =  dictDao.queryByCondition(query);
        queryListAfter(ret.getList());
        return ret;
    }

    public void batchDelCoreDict(List<Long> ids){
        try {
        	//TODO,找到數據字典所有子類，設定刪除標記
            dictDao.batchDelCoreDictByIds(ids);
        } catch (Exception e) {
            throw new PlatformException("批量刪除CoreDict失敗", e);
        }
    }
    public List<CoreDict> queryExcel(PageQuery<CoreUser> query) {
        //同查詢，不需要額外數據
        PageQuery ret =  dictDao.queryByCondition(query);
        queryListAfter(ret.getList());
        return ret.getList();
        
    }
    /**
     *  參考：dict_mapping.xml
     * @param list
     * @return
     */
    public void batchInsert(List<DictExcelImportData> list) {
       int dataStartRow = 2;
       final Map<Integer,DictExcelImportData> map = new HashMap<>();
       list.forEach((item)->map.put(item.getExcelId(), item));
       //逐個按照順序導入
       for(DictExcelImportData item:list) {
           CoreDict dict = new  CoreDict();
           dict.setName(item.getName());
           dict.setRemark(item.getRemark());
           dict.setType(item.getType());
           dict.setTypeName(item.getTypeName());
           dict.setValue(item.getValue());
           
           //設定父字典
           if(item.getParentExcelId()!=0) {
               DictExcelImportData parentItem =  map.get(item.getParentExcelId());
               if(parentItem==null) {
                   //硬編碼，TODO,用reader缺少校驗，替換手寫導入
                   int row = item.getExcelId()+dataStartRow;
                   throwImporError(row,5,"未找到父字典");
               }
               if(parentItem.getId()==null) {
                   int row = item.getExcelId()+dataStartRow;
                   throwImporError(row,5,"父字典未被導入，請先導入父字典");
               }
               dict.setParent(parentItem.getId());
           }
           dict.setCreateTime(new Date());
           //導入前先查找是否有此值
           CoreDict template = new CoreDict();
           template.setType(dict.getType());
           template.setValue(dict.getValue());
           CoreDict dbDict = dictDao.templateOne(template);
           if(dbDict!=null) {
               int row = item.getExcelId()+dataStartRow;
               throwImporError(row,0,"字典數據已經存在");
           }
           dictDao.insert(dict);
           
           item.setId(dict.getId());
           dataStartRow++;
       }
       
       
    }
    
    private void throwImporError(int row,int col,String msg) {
        ExcelError error = new ExcelError();
        CellReference cr = new CellReference(row,col,false,false);
        error.setCell(cr.formatAsString());
        error.setMsg(msg);
        throw new PlatformException("導入錯誤在:"+error.getCell()+","+msg);
    }
    
    
}