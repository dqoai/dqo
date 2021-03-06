/*
 * Copyright © 2021 DQO.ai (support@dqo.ai)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ai.dqo.metadata.storage.localfiles.userhome;

import ai.dqo.cli.completion.completers.cache.CliCompletionCache;
import ai.dqo.core.filesystem.virtual.FolderTreeNode;
import ai.dqo.metadata.userhome.UserHome;

/**
 * File based user home context used to operate on the user home file based model.
 */
public class UserHomeContext {
    private final FolderTreeNode homeRoot;
    private UserHome userHome;
    private UserHomeContextCache userHomeContextCache;

    /**
     * Creates a user home context given a virtual folder with the user home.
     * @param homeRoot User home root folder.
     */
    public UserHomeContext(FolderTreeNode homeRoot) {
        this.homeRoot = homeRoot;
    }

    /**
     * Home file system root based on the file based implementation.
     * @return User home root.
     */
    public FolderTreeNode getHomeRoot() {
        return homeRoot;
    }

    /**
     * Returns a user home model that is used by this instance.
     * @return User home model connected to the file system.
     */
    public UserHome getUserHome() {
        return userHome;
    }

    /**
     * Sets a reference to the user home model.
     * @param userHome User home model
     */
    public void setUserHome(UserHome userHome) {
        assert userHome instanceof FileUserHomeImpl &&  ((FileUserHomeImpl)userHome).getHomeFolder() == this.homeRoot;
        this.userHome = userHome;
    }

    /**
     * Flushes changes to disk.
     */
    public void flush() {
		this.userHome.flush(); // flushes changes to the virtual file system
		this.homeRoot.flush(); // flushes changes to disk

        CliCompletionCache.invalidateCache();

        if (this.userHomeContextCache != null) {
            this.userHomeContextCache.invalidateCache();
        }
    }

    /**
     * Associates the user home context with a user home context cache. The cache is flushed when the user context is persisted.
     */
    protected void setUserModelCache(UserHomeContextCache userHomeContextCache) {
        this.userHomeContextCache = userHomeContextCache;
    }
}
