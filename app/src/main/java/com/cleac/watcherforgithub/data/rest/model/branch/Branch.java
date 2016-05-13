
package com.cleac.watcherforgithub.data.rest.model.branch;

import com.google.gson.annotations.Expose;

public class Branch {

    @Expose
    private String name;
    @Expose
    private Commit commit;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The commit
     */
    public Commit getCommit() {
        return commit;
    }

    /**
     * 
     * @param commit
     *     The commit
     */
    public void setCommit(Commit commit) {
        this.commit = commit;
    }

}
