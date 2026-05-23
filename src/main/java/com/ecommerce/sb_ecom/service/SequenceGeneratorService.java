package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.DatabaseSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SequenceGeneratorService {

    @Autowired
    private MongoOperations mongoOperations;

    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
             new Query(where("_id").is(seqName)),
             new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class
        );
        return counter != null ? counter.getSeq() : 1;
    }
}
