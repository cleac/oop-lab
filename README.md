# oop-lab
Lab for Object Oriented Programming course - watcher for GitHub
 Project that allows to get users data and get commits from GitHub developer network

### Workflow 


- 1 week. Implement basic UI. Changed files:
 - app/src/main/java/com/cleac/watcherforgithub/ui/CommitDetailsActivity.java  
 - app/src/main/java/com/cleac/watcherforgithub/ui/CommitsListFragment.java  
 - app/src/main/java/com/cleac/watcherforgithub/ui/RepositoryActivity.java     
 - app/src/main/java/com/cleac/watcherforgithub/ui/RepositoryListFragment.java
 - app/src/main/java/com/cleac/watcherforgithub/ui/CommitsListAdapter.java     
 - app/src/main/java/com/cleac/watcherforgithub/ui/MainActivity.java         
 - app/src/main/java/com/cleac/watcherforgithub/ui/RepositoryListAdapter.java  
 - app/src/main/java/com/cleac/watcherforgithub/ui/UserDetails.java
 - app/src/main/res/layout/activity_repository.xml
 - app/src/main/res/layout/dialog_add_repo.xml
 - app/src/main/res/layout/fragment_commit_details.xml
 - app/src/main/res/layout/activity_commit_details.xml
 - app/src/main/res/layout/repo_list_node.xml
 - app/src/main/res/layout/commit_list_node.xml
 - app/src/main/res/layout/fragment_repository.xml
 - app/src/main/res/layout/activity_user_details.xml
 - app/src/main/res/layout/fragment_main.xml
 - app/src/main/res/layout/activity_main.xml
 - app/src/main/res/layout/fragment_user_details.xml
 - app/src/main/res/menu/menu_repository.xml
 - app/src/main/res/menu/menu_commit_details.xml
 - app/src/main/res/menu/menu_user_details.xml
 - app/src/main/res/menu/menu_main.xml
 - app/src/main/res/values/strings.xml
 - app/src/main/res/values/dimens.xml
 - app/src/main/res/values/styles.xml
 - app/src/main/res/xml/sync_adapter.xml
 - app/src/main/res/xml/authenticator.xml
 - app/src/main/res/values-w820dp/dimens.xml
 - app/src/main/AndroidManifest.xml

- 2 week. Add possibility to download data using Github REST API and add unit test for it. Changed files:
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/service/GithubApiService.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/RestError.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/RestClient.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/UserData.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/committs/Author.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/committs/Committer.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/committs/Tree.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/committs/CommitsDatum.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/committs/Committer_.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/committs/Author_.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/committs/Commit.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/repo/RepoInfo.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/repo/Owner.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/branch/Commit.java
 - app/src/main/java/com/cleac/watcherforgithub/data/rest/model/branch/Branch.java
 - app/src/androidTest/java/com/cleac/watcherforgithub/data/rest/TestGetInfo.java

- 3 week. Implement database model, data access, sync possibility and unit tests for database. Changed files:
 - app/src/main/java/com/cleac/watcherforgithub/data/sync/GithubAuthenticator.java
 - app/src/main/java/com/cleac/watcherforgithub/data/sync/SyncService.java
 - app/src/main/java/com/cleac/watcherforgithub/data/DataContract.java
 - app/src/main/java/com/cleac/watcherforgithub/data/DataDBHelper.java
 - app/src/main/java/com/cleac/watcherforgithub/data/DataProvider.java
 - app/src/main/java/com/cleac/watcherforgithub/data/sync/GithubAuthenticatorService.java
 - app/src/androidTest/java/com/cleac/watcherforgithub/data/TestDB.java

- 4 week. Implement ContentProvider and adapters, unit tests for adapters. Changed files:
 - app/src/androidTest/java/com/cleac/watcherforgithub/data/TestProvider.java

- 5 week. Add adapters support to UI; kind of finalize object. Changed files:
 - app/src/main/java/com/cleac/watcherforgithub/data/sync/SyncAdapter.java
 - app/src/main/java/com/cleac/watcherforgithub/ui/CommitsListAdapter.java
 - app/src/main/java/com/cleac/watcherforgithub/ui/RepositoryListAdapter.java
 - app/src/main/java/com/cleac/watcherforgithub/ui/CommitDetailsActivity.java  
 - app/src/main/java/com/cleac/watcherforgithub/ui/CommitsListFragment.java  
 - app/src/main/java/com/cleac/watcherforgithub/ui/RepositoryActivity.java     
 - app/src/main/java/com/cleac/watcherforgithub/ui/RepositoryListFragment.java
 - app/src/main/java/com/cleac/watcherforgithub/ui/CommitsListAdapter.java     
 - app/src/main/java/com/cleac/watcherforgithub/ui/MainActivity.java         
 - app/src/main/java/com/cleac/watcherforgithub/ui/RepositoryListAdapter.java  
 - app/src/main/java/com/cleac/watcherforgithub/ui/UserDetails.java
 - app/src/main/res/layout/activity_repository.xml
 - app/src/main/res/layout/dialog_add_repo.xml
 - app/src/main/res/layout/fragment_commit_details.xml
 - app/src/main/res/layout/activity_commit_details.xml
 - app/src/main/res/layout/repo_list_node.xml
 - app/src/main/res/layout/commit_list_node.xml
 - app/src/main/res/layout/fragment_repository.xml
 - app/src/main/res/layout/activity_user_details.xml
 - app/src/main/res/layout/fragment_main.xml
 - app/src/main/res/layout/activity_main.xml
 - app/src/main/res/layout/fragment_user_details.xml
 - app/src/main/res/menu/menu_repository.xml
 - app/src/main/res/menu/menu_commit_details.xml
 - app/src/main/res/menu/menu_user_details.xml
 - app/src/main/res/menu/menu_main.xml
 - app/src/main/res/values/strings.xml
 - app/src/main/res/values/dimens.xml
 - app/src/main/res/values/styles.xml
 - app/src/main/res/values-w820dp/dimens.xml
 - app/src/main/AndroidManifest.xml

