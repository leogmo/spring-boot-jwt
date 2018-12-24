package com.concrete.challenge.users.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.concrete.challenge.users.entities.AppUser;

public class RepositoryMock {
	
	public static List<AppUser> users = new ArrayList<AppUser>() {
		{
			add(new AppUser() {
				{
					 setEmail("joao@silva.org");
					 setId(1);
					 setName("Joao da Silva");
					 setPassword("$2a$10$cCjcKsTw0VzEZAsQwKnjpuXptVe4zeampGfYtjMjxhTNckqxHr8VC");
					 setCreated(new Date());
					 setLastLogin(new Date());
					 setModified(new Date());
					 //setToken("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvQHNpbHZhLm9yZyIsImV4cCI6MTU0NTU4Njk1Mn0.AcASU3KEi8yvNyIMtucHu4Yvn8oNfAqvzsUYacHf-BTLawGT_DMF4oWkTYmdzmX_JuVCj3jn7VGm9GkvhedyhQ");
					 
				}
			});
			add(new AppUser() {
				{
					 setEmail("jose@silva.org");
					 setId(2);
					 setName("Jose da Silva");
					 setPassword("$2a$10$zBvbYZTgvcRwnzq82Zf5KeQVn0P.5Phb2UaStvilpXQ8Q/7uKRH3u");
					 setCreated(new Date());
					 setLastLogin(new Date());
					 setModified(new Date());
					 //setToken("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvQHNpbHZhLm9yZyIsImV4cCI6MTU0NTUyNDEzMn0.n5wZv58_RSQF0ssBH9LXYiqIJsv4Y28tzvOM5vlUcWq2dRNMDL3yO54FiDS8RFQ8j3cOkNS2PcDet250w2giiA");
					 
				}
			});
		}
	};
}
