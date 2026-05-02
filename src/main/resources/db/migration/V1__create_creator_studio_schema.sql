-- =========================================
-- 拡張
-- =========================================
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================================
-- m_users
-- =========================================
CREATE TABLE m_users (
                         id BIGSERIAL PRIMARY KEY,
                         uuid VARCHAR(36) NOT NULL UNIQUE,
                         name VARCHAR(50) NOT NULL,
                         email VARCHAR(255),

                         created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                         created_user_id VARCHAR(36) NOT NULL,
                         updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                         updated_user_id VARCHAR(36) NOT NULL,
                         is_deleted BOOLEAN NOT NULL
);

CREATE INDEX ix_m_users_is_deleted ON m_users(is_deleted);

-- =========================================
-- m_media_types
-- =========================================
CREATE TABLE m_media_types (
                               id BIGSERIAL PRIMARY KEY,
                               uuid VARCHAR(36) NOT NULL UNIQUE,

                               media_type_name VARCHAR(50) NOT NULL,
                               description VARCHAR(100),

                               created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               created_user_id VARCHAR(36) NOT NULL,
                               updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               updated_user_id VARCHAR(36) NOT NULL,
                               is_deleted BOOLEAN NOT NULL,

                               CONSTRAINT uk_m_media_types_media_type_name UNIQUE (media_type_name)
);

CREATE INDEX ix_m_media_types_is_deleted ON m_media_types(is_deleted);

-- =========================================
-- m_upload_statuses
-- =========================================
CREATE TABLE m_upload_statuses (
                                   id BIGSERIAL PRIMARY KEY,
                                   uuid VARCHAR(36) NOT NULL UNIQUE,

                                   status_code VARCHAR(20) NOT NULL,
                                   status_name VARCHAR(50) NOT NULL,
                                   description VARCHAR(255),

                                   created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                   created_user_id VARCHAR(36) NOT NULL,
                                   updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                   updated_user_id VARCHAR(36) NOT NULL,
                                   is_deleted BOOLEAN NOT NULL,

                                   CONSTRAINT uk_m_upload_statuses_status_code UNIQUE (status_code)
);

CREATE INDEX ix_m_upload_statuses_is_deleted ON m_upload_statuses(is_deleted);

-- =========================================
-- t_content_projects
-- =========================================
CREATE TABLE t_content_projects (
                                    id BIGSERIAL PRIMARY KEY,
                                    uuid VARCHAR(36) NOT NULL UNIQUE,

                                    project_name VARCHAR(50) NOT NULL,
                                    project_summary TEXT,

                                    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                    created_user_id VARCHAR(36) NOT NULL,
                                    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                    updated_user_id VARCHAR(36) NOT NULL,
                                    is_deleted BOOLEAN NOT NULL
);

-- =========================================
-- t_media_assets
-- =========================================
CREATE TABLE t_media_assets (
                                id BIGSERIAL PRIMARY KEY,
                                uuid VARCHAR(36) NOT NULL UNIQUE,

                                original_file_name VARCHAR(100) NOT NULL,
                                saved_file_name VARCHAR(100) NOT NULL,
                                mime_type VARCHAR(100),
                                storage_path VARCHAR(1000) NOT NULL,
                                file_size BIGINT NOT NULL,
                                memo VARCHAR(1000),

                                project_id BIGINT NOT NULL,
                                media_type_id BIGINT NOT NULL,

                                created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                created_user_id VARCHAR(36) NOT NULL,
                                updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                updated_user_id VARCHAR(36) NOT NULL,
                                is_deleted BOOLEAN NOT NULL,

                                CONSTRAINT fk_media_assets_project
                                    FOREIGN KEY (project_id) REFERENCES t_content_projects(id),

                                CONSTRAINT fk_media_assets_media_type
                                    FOREIGN KEY (media_type_id) REFERENCES m_media_types(id)
);

CREATE INDEX ix_t_media_assets_is_deleted ON t_media_assets(is_deleted);

-- =========================================
-- t_upload_jobs
-- =========================================
CREATE TABLE t_upload_jobs (
                               id BIGSERIAL PRIMARY KEY,
                               uuid VARCHAR(36) NOT NULL UNIQUE,

                               file_name VARCHAR(100) NOT NULL,
                               storage_path VARCHAR(1000) NOT NULL,

                               upload_status_id BIGINT NOT NULL,

                               created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               created_user_id VARCHAR(36) NOT NULL,
                               updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               updated_user_id VARCHAR(36) NOT NULL,
                               is_deleted BOOLEAN NOT NULL,

                               CONSTRAINT fk_upload_jobs_status
                                   FOREIGN KEY (upload_status_id) REFERENCES m_upload_statuses(id)
);

CREATE INDEX ix_t_upload_jobs_is_deleted ON t_upload_jobs(is_deleted);
CREATE INDEX ix_t_upload_jobs_status_id ON t_upload_jobs(upload_status_id);

-- =========================================
-- 初期データ: m_upload_statuses
-- =========================================
INSERT INTO m_upload_statuses (
    uuid,
    status_code,
    status_name,
    description,
    created_user_id,
    updated_user_id,
    created_at,
    updated_at,
    is_deleted
) VALUES
      (
          gen_random_uuid()::text,
          '10',
          'アップロード受付',
          'アップロードを受け付けた状態',
          '7e525b47-6817-4b6f-8cb0-f138ca01f00f',
          '7e525b47-6817-4b6f-8cb0-f138ca01f00f',
          CURRENT_TIMESTAMP,
          CURRENT_TIMESTAMP,
          FALSE
      ),
      (
          gen_random_uuid()::text,
          '20',
          '登録処理中',
          '非同期処理中',
          '7e525b47-6817-4b6f-8cb0-f138ca01f00f',
          '7e525b47-6817-4b6f-8cb0-f138ca01f00f',
          CURRENT_TIMESTAMP,
          CURRENT_TIMESTAMP,
          FALSE
      ),
      (
          gen_random_uuid()::text,
          '30',
          '登録完了',
          '処理成功',
          '7e525b47-6817-4b6f-8cb0-f138ca01f00f',
          '7e525b47-6817-4b6f-8cb0-f138ca01f00f',
          CURRENT_TIMESTAMP,
          CURRENT_TIMESTAMP,
          FALSE
      ),
      (
          gen_random_uuid()::text,
          '40',
          '登録失敗',
          'エラー発生',
          '7e525b47-6817-4b6f-8cb0-f138ca01f00f',
          '7e525b47-6817-4b6f-8cb0-f138ca01f00f',
          CURRENT_TIMESTAMP,
          CURRENT_TIMESTAMP,
          FALSE
      );