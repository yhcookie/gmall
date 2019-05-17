package com.atguigu.gmall.list;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallListServiceApplicationTests {

	@Autowired
	private JestClient jestClient;

	@Test
	public void contextLoads() {
	}

	@Test //dsl在哪执行啊  index/type/_search
	public void testE() throws IOException {

		String strQuery = "{\n" +
				"  \"query\": {\n" +
				"    \"match\": {\n" +
				"      \"actorList.name\": \"张译\"\n" +
				"    }\n" +
				"  }\n" +
				"}";
		Search search = new Search.Builder(strQuery).addIndex("movie_chn").addType("movie_type_chn").build();

		SearchResult result = jestClient.execute(search);

		List<SearchResult.Hit<HashMap, Void>> hits = result.getHits(HashMap.class);
		for (SearchResult.Hit<HashMap, Void> hit : hits) {
			HashMap map = hit.source;
			String name = (String) map.get("name");
			System.out.println("name:" + name);
			System.out.println("sourceMap:" + map);
		}
	}

}
