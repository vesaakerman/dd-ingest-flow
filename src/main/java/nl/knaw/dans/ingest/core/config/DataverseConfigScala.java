/*
 * Copyright (C) 2022 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.ingest.core.config;

import java.net.URI;

public class DataverseConfigScala {
    private URI baseUrl;
    private int apiVersion;
    private int connectionTimeoutMs;
    private int readTimeoutMs;
    private String apiKey;
    private String unblockKey;
    private int publishAwaitUnlockMaxRetries;
    private int publishAwaitUnlockWaitTimeMs;
    private int awaitUnlockMaxRetries;
    private int awaitUnlockWaitTimeMs;

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUnblockKey() {
        return unblockKey;
    }

    public void setUnblockKey(String unblockKey) {
        this.unblockKey = unblockKey;
    }

    public int getPublishAwaitUnlockMaxRetries() {
        return publishAwaitUnlockMaxRetries;
    }

    public void setPublishAwaitUnlockMaxRetries(int publishAwaitUnlockMaxRetries) {
        this.publishAwaitUnlockMaxRetries = publishAwaitUnlockMaxRetries;
    }

    public int getPublishAwaitUnlockWaitTimeMs() {
        return publishAwaitUnlockWaitTimeMs;
    }

    public void setPublishAwaitUnlockWaitTimeMs(int publishAwaitUnlockWaitTimeMs) {
        this.publishAwaitUnlockWaitTimeMs = publishAwaitUnlockWaitTimeMs;
    }

    public int getAwaitUnlockMaxRetries() {
        return awaitUnlockMaxRetries;
    }

    public void setAwaitUnlockMaxRetries(int awaitUnlockMaxRetries) {
        this.awaitUnlockMaxRetries = awaitUnlockMaxRetries;
    }

    public int getAwaitUnlockWaitTimeMs() {
        return awaitUnlockWaitTimeMs;
    }

    public void setAwaitUnlockWaitTimeMs(int awaitUnlockWaitTimeMs) {
        this.awaitUnlockWaitTimeMs = awaitUnlockWaitTimeMs;
    }
}
