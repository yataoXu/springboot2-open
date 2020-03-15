package cn.myframe.repository;

import cn.myframe.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/4/22/022 18:21
 * @Version 1.0
 */
@Repository
public interface UserRepository extends SolrCrudRepository<UserDto, String> {

    @Query("address:*?0*")
    public List<UserDto> findByQueryAnnotation(String searchTerm, Sort sort);

    @Query(value="address:?0",/*fields ={"address"},*/filters = {"id:?1"})
    public List<UserDto> findByPage(String searchTerm,String idTerm, Pageable Pageable);

}
