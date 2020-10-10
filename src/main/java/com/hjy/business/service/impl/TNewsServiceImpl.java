package com.hjy.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjy.business.dao.TNewsMapper;
import com.hjy.business.entity.TNews;
import com.hjy.business.service.TNewsService;
import com.hjy.common.domin.CommonResult;
import com.hjy.common.utils.IDUtils;
import com.hjy.common.utils.JsonUtil;
import com.hjy.common.utils.page.PageUtils;
import com.hjy.system.entity.SysToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * (TNews)表服务实现类
 *
 * @author wangdengjun
 * @since 2020-07-27 16:15:29
 */
@Service
public class TNewsServiceImpl implements TNewsService {
    @Autowired
    private TNewsMapper tNewsMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param parm 主键
     * @return 实例对象
     */
    @Override
    public CommonResult selectById(String parm) throws Exception{
        JSONObject jsonObject = JSON.parseObject(parm);
        String pkNewsId=String.valueOf(jsonObject.get("pkNewsId"));
        TNews tNews = tNewsMapper.selectById(pkNewsId);
        return new CommonResult(200,"success","数据获取成功!",tNews);
    }

    /**
     * 新增数据
     * @param tNews
     * @param sysToken
     * @param newsType
     * @return
     * @throws Exception
     */
    @Override
    public CommonResult insertSelective(TNews tNews, SysToken sysToken,Integer newsType) throws Exception{
        tNews.setPkNewsId(IDUtils.getUUID());
        tNews.setNewsType(newsType);
        tNews.setCreateDate(new Date());
        tNews.setCreateUserId(sysToken.getFkUserId());
        tNews.setLastModifyDate(tNews.getCreateDate());
        tNews.setLastModifyUserId(tNews.getCreateUserId());
        tNewsMapper.insertSelective(tNews);
        return new CommonResult(200,"success","数据添加成功!",null);
    }

    /**
     * 修改数据
     * @param tNews
     * @param sysToken
     * @return
     * @throws Exception
     */
    @Override
    public CommonResult updateById(TNews tNews, SysToken sysToken) throws Exception{
        tNewsMapper.updateById(tNews);
        return new CommonResult(200,"success","数据修改成功!",null);
    }

    /**
     * 通过主键删除数据
     *
     * @param parm 主键
     * @return 是否成功
     */
    @Override
    public CommonResult deleteById(String parm) throws Exception{
        JSONObject jsonObject = JSON.parseObject(parm);
        String pkNewsId=String.valueOf(jsonObject.get("pkNewsId"));
        tNewsMapper.deleteById(pkNewsId);
        return new CommonResult(200,"success","数据删除成功!",null);
    }
    

    @Override
    public CommonResult selectAllPage(String param,Integer newsType) {
        JSONObject json = JSON.parseObject(param);
        //实体数据
        String pageNumStr = JsonUtil.getStringParam(json,"pageNum");
        String pageSizeStr = JsonUtil.getStringParam(json,"pageSize");
        String newsTitle = JsonUtil.getStringParam(json,"newsTitle");
        String newsStatus = JsonUtil.getStringParam(json,"newsStatus");
        TNews tNews = new TNews();
        tNews.setNewsTitle(newsTitle);
        tNews.setNewsStatus(Integer.parseInt(newsStatus));
        tNews.setNewsType(newsType);
        //分页记录条数
        int pageNum = 1;
        int pageSize = 10;
        if(pageNumStr != null){
            pageNum = Integer.parseInt(pageNumStr);
        }
        if(pageSizeStr != null){
            pageSize = Integer.parseInt(pageSizeStr);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<TNews> tNewss = tNewsMapper.selectAllPage(tNews);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("PageResult",PageUtils.getPageResult(new PageInfo<TNews>(tNewss)));
        return new CommonResult(200,"success","查询数据成功!",jsonObject);
    }

}