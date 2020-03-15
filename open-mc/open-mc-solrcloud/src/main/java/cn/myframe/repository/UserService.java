package cn.myframe.repository;

import cn.myframe.dto.Item;
import cn.myframe.dto.UserDto;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/4/22/022 18:26
 * @Version 1.0
 */
@Service
public class UserService {

    @Resource
    UserRepository userRepository;

    @Resource
    private SolrTemplate solrTemplate;

    public void save(UserDto userDto){
        userRepository.save(userDto);
    }

    public List<UserDto> query(String search){
        return userRepository.findByQueryAnnotation(search,new Sort(Sort.Direction.ASC, "id"));
    }

    public List<UserDto> queryByPage(String search,String id){
        Pageable pageable = PageRequest.of(0,3,new Sort(Sort.Direction.ASC, "id"));
        return userRepository.findByPage(search,id,pageable);
    }

    /**
     * 高亮
     * @param address
     */
    public List<Item> queryHeightLight(String address){
        List<Item> itemList = new ArrayList<>();
        HighlightQuery highlightQuery = new SimpleHighlightQuery(new SimpleStringCriteria("address:开平"));
        HighlightOptions options = new HighlightOptions();
        options.addField("address");
        options.setSimplePrefix("<br>");
        options.setSimplePostfix("</br>");
        highlightQuery.setHighlightOptions(options);
        HighlightPage<Item> page = solrTemplate.queryForHighlightPage("demo",highlightQuery,Item.class);
        //获取高亮数据
        List<HighlightEntry<Item>> highlighted = page.getHighlighted();
        for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
            //获取SKU信息
            Item item = itemHighlightEntry.getEntity();
            //获取高亮数据
            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
            //高亮数据
            if(highlights!=null && highlights.size()>0 && highlights.get(0).getSnipplets()!=null && highlights.get(0).getSnipplets().size()>0){
                String snipplets = highlights.get(0).getSnipplets().get(0);
                //替换高亮数据
                item.setTitle(snipplets);
                itemList.add(item);
            }
        }
        return itemList;

    }


    /**
     * 创建文件索引
     */
    public void upFile(){
        userRepository.deleteAll();
        ContentStreamUpdateRequest up = new ContentStreamUpdateRequest(
                "/update/extract");
        File file = new File("C:/Users/Administrator/Desktop/demo/test.txt");
        try{
            up.addFile(file,"text/plain");
            up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
            CloudSolrClient solrClient = ((CloudSolrClient)solrTemplate.getSolrClient());
            solrClient.setDefaultCollection("demo");
            solrClient.request(up);
            QueryResponse query = solrClient.query(new SolrQuery("*:*"));
            SolrDocumentList results = query.getResults();
            System.out.println(results);
        }catch (Exception e){

        }

    }


}
