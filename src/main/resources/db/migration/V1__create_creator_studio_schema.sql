CREATE TABLE m_users (
    user_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX ix_m_users_is_deleted ON m_users (is_deleted);

CREATE TABLE m_media_types (
    media_type_id VARCHAR(36) PRIMARY KEY,
    media_type_name VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    created_user_id VARCHAR(36) NOT NULL,
    updated_user_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_m_media_types_media_type_name UNIQUE (media_type_name),
    CONSTRAINT fk_m_media_types_created_user FOREIGN KEY (created_user_id) REFERENCES m_users (user_id),
    CONSTRAINT fk_m_media_types_updated_user FOREIGN KEY (updated_user_id) REFERENCES m_users (user_id)
);

CREATE INDEX ix_m_media_types_is_deleted ON m_media_types (is_deleted);

CREATE TABLE t_content_projects (
    project_id VARCHAR(36) PRIMARY KEY,
    project_name VARCHAR(50) NOT NULL,
    project_summry TEXT,
    created_user_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_t_content_projects_created_user FOREIGN KEY (created_user_id) REFERENCES m_users (user_id)
);

CREATE TABLE m_upload_statuses (
    status_id VARCHAR(36) PRIMARY KEY,
    status_name VARCHAR(20) NOT NULL,
    created_user_id VARCHAR(36) NOT NULL,
    updated_user_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_m_upload_statuses_status_name UNIQUE (status_name),
    CONSTRAINT fk_m_upload_statuses_created_user FOREIGN KEY (created_user_id) REFERENCES m_users (user_id),
    CONSTRAINT fk_m_upload_statuses_updated_user FOREIGN KEY (updated_user_id) REFERENCES m_users (user_id)
);

CREATE INDEX ix_m_upload_statuses_is_deleted ON m_upload_statuses (is_deleted);

CREATE TABLE t_media_assets (
    media_asset_id VARCHAR(36) PRIMARY KEY,
    media_type_id VARCHAR(36),
    project_id VARCHAR(36),
    original_file_name VARCHAR(100) NOT NULL,
    saved_file_name VARCHAR(100) NOT NULL,
    mime_type VARCHAR(100),
    storage_path VARCHAR(1000) NOT NULL,
    file_size BIGINT NOT NULL,
    memo VARCHAR(1000),
    created_user_id VARCHAR(36) NOT NULL,
    updated_user_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_t_media_assets_media_type FOREIGN KEY (media_type_id) REFERENCES m_media_types (media_type_id),
    CONSTRAINT fk_t_media_assets_project FOREIGN KEY (project_id) REFERENCES t_content_projects (project_id),
    CONSTRAINT fk_t_media_assets_created_user FOREIGN KEY (created_user_id) REFERENCES m_users (user_id),
    CONSTRAINT fk_t_media_assets_updated_user FOREIGN KEY (updated_user_id) REFERENCES m_users (user_id)
);

CREATE INDEX ix_t_media_assets_is_deleted ON t_media_assets (is_deleted);

CREATE TABLE t_upload_jobs (
    job_id VARCHAR(36) PRIMARY KEY,
    file_name VARCHAR(100) NOT NULL,
    storage_path VARCHAR(1000) NOT NULL,
    upload_status VARCHAR(20) NOT NULL DEFAULT '10',
    created_user_id VARCHAR(36) NOT NULL,
    updated_user_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_t_upload_jobs_upload_status FOREIGN KEY (upload_status) REFERENCES m_upload_statuses (status_id),
    CONSTRAINT fk_t_upload_jobs_created_user FOREIGN KEY (created_user_id) REFERENCES m_users (user_id),
    CONSTRAINT fk_t_upload_jobs_updated_user FOREIGN KEY (updated_user_id) REFERENCES m_users (user_id)
);

CREATE INDEX ix_t_upload_jobs_is_deleted ON t_upload_jobs (is_deleted);

INSERT INTO m_users (user_id, name, email, created_at, updated_at, is_deleted)
VALUES ('7e525b47-6817-4b6f-8cb0-f138ca01f00f', 'admin', 'admin@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO m_media_types (
    media_type_id,
    media_type_name,
    description,
    created_user_id,
    updated_user_id,
    created_at,
    updated_at,
    is_deleted
)
VALUES
    ('10000000-0000-0000-0000-000000000001', '動画', 'mp4などの動画素材', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('10000000-0000-0000-0000-000000000002', '画像', 'jpg/pngなどの画像素材', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('10000000-0000-0000-0000-000000000003', '音声', 'BGM・ナレーション音声', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('10000000-0000-0000-0000-000000000004', '台本', '動画台本テキスト', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO m_upload_statuses (
    status_id,
    status_name,
    created_user_id,
    updated_user_id,
    created_at,
    updated_at,
    is_deleted
)
VALUES
    ('10', 'アップロード受付', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('20', '登録処理中', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('30', '登録完了', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('40', '登録失敗', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', '7e525b47-6817-4b6f-8cb0-f138ca01f00f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);
