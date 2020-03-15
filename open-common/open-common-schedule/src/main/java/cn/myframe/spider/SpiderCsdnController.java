package cn.myframe.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpiderCsdnController {

    @Autowired
    SpiderCsdn spiderCsdn;

    @RequestMapping("/change/{author}")
    public String changeAuthor(@PathVariable String author){
        spiderCsdn.changeAuthor(author);
        return author;
    }

    @RequestMapping("/u/{size}")
    public String updateArticle(@PathVariable int size){
        spiderCsdn.artical(size,0);
        return "success";
    }
}
