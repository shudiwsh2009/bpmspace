package com.chinamobile.bpmspace.core.data;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.chinamobile.bpmspace.core.domain.index.IndexCategory;
import com.chinamobile.bpmspace.core.domain.index.IndexDescriptor;
import com.chinamobile.bpmspace.core.domain.index.IndexState;
import com.mongodb.DBCollection;

public class IndexMongoAccess {

	public void addIndexDescriptor(IndexDescriptor indexDescriptor) {
		MongoAccess.MONGO.save(indexDescriptor);
	}

	public void deleteIndexDescriptor(String id) {
		MongoAccess.MONGO.remove(new Query(Criteria.where("id").is(id)),
				IndexDescriptor.class);
	}

	public void updateIndexDescriptorSatete(String id, IndexState state) {
		MongoAccess.MONGO.updateFirst(new Query(Criteria.where("id").is(id)),
				new Update().set("state", state), IndexDescriptor.class);
	}

	public List<IndexDescriptor> getIndexDescriptorList(IndexCategory indexCat) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("category").is(indexCat)),
				IndexDescriptor.class);
	}

	public List<IndexDescriptor> getStartedIndexDescriptorList(
			IndexCategory indexCat) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("category").is(indexCat)
						.andOperator(Criteria.where("state").is("START"))),
				IndexDescriptor.class);
	}

	public List<IndexDescriptor> getConstructedIndexDescriptorList(
			IndexCategory indexCat) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("state").is(IndexState.START)
						.andOperator(Criteria.where("category").is(indexCat))),
				IndexDescriptor.class);
	}

	public List<IndexDescriptor> getUnconstructedIndexDescriptorList(
			IndexCategory indexCat) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("state").is(IndexState.STOP)
						.andOperator(Criteria.where("category").is(indexCat))),
				IndexDescriptor.class);
	}

	public List<IndexDescriptor> getIndexDescriptorByClassName(String cn) {
		return MongoAccess.MONGO.find(new Query(Criteria.where("class_name")
				.is(cn)), IndexDescriptor.class);
	}

	public List<IndexDescriptor> getIndexDescriptorByType(String type) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("type").is(type)),
				IndexDescriptor.class);
	}

	public List<IndexDescriptor> getStartedIndexDescriptorByType(String type) {
		return MongoAccess.MONGO.find(new Query(Criteria.where("type").is(type)
				.andOperator(Criteria.where("state").is("START"))),
				IndexDescriptor.class);
	}

	public IndexDescriptor getIndexDescriptorByID(String ID) {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("id").is(ID)), IndexDescriptor.class);
	}

	public boolean checkCollectionExistes(String collectionName) {
		return MongoAccess.MONGO.collectionExists(collectionName);
	}

	public void dropCollection(String collectionName) {
		MongoAccess.MONGO.dropCollection(collectionName);
	}

	public DBCollection createCollection(String collectionName) {
		return MongoAccess.MONGO.createCollection(collectionName);
	}
}
