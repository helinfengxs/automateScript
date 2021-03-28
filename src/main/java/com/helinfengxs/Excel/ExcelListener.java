package com.helinfengxs.Excel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * easyExcel监听类
 */
public class ExcelListener extends AnalysisEventListener<ExcelEntity> {

    private final HashMap<String, Object> excelData = new HashMap<>(); //最终得到的Excel Hashmap集合数据
    private ArrayList<Object> listData; //用户构造用例集的数组对象
    private SaveData saveData; //最终数据保存对象

    /**
     * 无参构造方法
     */
    public ExcelListener() {

    }

    /**
     * 有参数构造方法，用与得到最终数据
     *
     * @param saveData 最终数据保存对象
     */
    public ExcelListener(SaveData saveData) {
        this.saveData = saveData;
    }

    /**
     * 一行一行读取Excel内容
     *
     * @param excelEntity     excel实体类对象
     * @param analysisContext 固体传入对象
     */
    @Override
    public void invoke(ExcelEntity excelEntity, AnalysisContext analysisContext) {
        try {

            String caseSuteName = excelEntity.getCaseSutiesName(); //得到用例集名称
            //判断用例名称是否为空
            if (caseSuteName != null) {

                listData = new ArrayList<>();  //判断用例集不为空，则去创建一个新的ArrayList对象
                //根据得到的用例集名称，put进map集合形成 {key=[]},再由创建的listData列表去添加用例集下面的子集。
                excelData.put(caseSuteName, listData);
            }
            listData.add(excelEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * excel读取完毕后执行的方法。
     *
     * @param analysisContext 固定传入对象
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData.setData(excelData);//得到最终的数据
    }
}
