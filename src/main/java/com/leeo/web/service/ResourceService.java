package com.leeo.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leeo.web.entity.Resource;
import com.leeo.web.repository.BaseRepository;
import com.leeo.web.repository.ResourceRepository;

@Service
@Transactional
public class ResourceService extends BaseService<Resource, Long> {

    private ResourceRepository resourceRepository;

    @Autowired
    @Override
    public void setRepository(BaseRepository<Resource, Long> resourceRepository) {
        this.baseRepository = resourceRepository;
        this.resourceRepository = (ResourceRepository) resourceRepository;
    }

    @Transactional(readOnly = true)
    public String findResourceIdentity(Resource resource) {

        if (resource == null) {
            return null;
        }

        StringBuilder s = new StringBuilder(resource.getIdentity());

        boolean hasResourceIdentity = !StringUtils.isEmpty(resource
            .getIdentity());

        Resource parent = findOne(resource.getParentId());
        while (parent != null) {
            if (!StringUtils.isEmpty(parent.getIdentity())) {
                s.insert(0, parent.getIdentity() + ":");
                hasResourceIdentity = true;
            }
            parent = findOne(parent.getParentId());
        }

        //如果用户没有声明 资源标识  且父也没有，那么就为空
        if (!hasResourceIdentity) {
            return "";
        }

        //如果最后一个字符是: 因为不需要，所以删除之
        int length = s.length();
        if (length > 0 && s.lastIndexOf(":") == length - 1) {
            s.deleteCharAt(length - 1);
        }

        //如果有儿子 最后拼一个*
        boolean hasChildren = false;
        for (Resource r : this.resourceRepository.findAll()) {
            if (resource.getId().equals(r.getParentId())) {
                hasChildren = true;
                break;
            }
        }
        if (hasChildren) {
            s.append(":*");
        }

        return s.toString();
    }

}