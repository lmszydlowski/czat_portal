-- Add missing klikId column to users table
ALTER TABLE users ADD COLUMN klik_id VARCHAR(255) NULL;

-- Create ENUM types first
CREATE TYPE billing_cycle AS ENUM ('monthly', 'yearly', 'weekly');
CREATE TYPE subscription_status AS ENUM ('active', 'canceled', 'paused', 'expired');
CREATE TYPE response_style AS ENUM ('friendly', 'professional', 'flirty', 'direct');

-- Users table modifications
ALTER TABLE users 
  ALTER COLUMN password_hash TYPE VARCHAR(512),
  ADD COLUMN email_verified BOOLEAN DEFAULT FALSE,
  ADD COLUMN verification_token VARCHAR(100),
  ADD COLUMN klik_id_added_at TIMESTAMP;

-- User profiles table improvements
ALTER TABLE user_profiles
  ALTER COLUMN full_name DROP NOT NULL,
  ADD COLUMN date_of_birth DATE,
  ADD COLUMN gender VARCHAR(50),
  ADD COLUMN location VARCHAR(100);

-- Payment information security enhancements
CREATE EXTENSION IF NOT EXISTS pgcrypto;
ALTER TABLE payment_information
  ALTER COLUMN cardholder_name SET DATA TYPE BYTEA USING pgp_sym_encrypt(cardholder_name::text, 'encryption_key'),
  ADD COLUMN encrypted_card_number BYTEA,
  ADD COLUMN expiry_month SMALLINT,
  ADD COLUMN expiry_year SMALLINT,
  DROP COLUMN expiry_date;

-- Subscription plans normalization
ALTER TABLE subscription_plans
  ALTER COLUMN billing_cycle TYPE billing_cycle USING billing_cycle::billing_cycle,
  ADD COLUMN currency VARCHAR(3) DEFAULT 'USD',
  ADD COLUMN trial_period_days INTEGER DEFAULT 0;

-- User subscriptions enhancements
ALTER TABLE user_subscriptions
  ADD COLUMN status subscription_status DEFAULT 'active',
  ADD COLUMN next_payment_date TIMESTAMP,
  ADD COLUMN stripe_subscription_id VARCHAR(255);

-- Bot preferences improvements
ALTER TABLE bot_preferences
  ALTER COLUMN response_style TYPE response_style USING response_style::response_style,
  ADD COLUMN language_preference VARCHAR(10) DEFAULT 'en',
  ADD COLUMN timezone VARCHAR(50) DEFAULT 'UTC';

-- Chat history optimizations
ALTER TABLE chat_history 
  ALTER COLUMN message_id TYPE UUID USING message_id::UUID,
  ADD COLUMN message_type VARCHAR(20) DEFAULT 'text',
  ADD COLUMN read_status BOOLEAN DEFAULT FALSE;

-- Create indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_klik_id ON users(klik_id);
CREATE INDEX idx_chat_history_user ON chat_history(user_id, timestamp);
