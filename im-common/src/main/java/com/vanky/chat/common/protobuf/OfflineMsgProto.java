// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: OfflineMsgProto.proto
// Protobuf Java Version: 4.26.0

package com.vanky.chat.common.protobuf;

public final class OfflineMsgProto {
  private OfflineMsgProto() {}
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 26,
      /* patch= */ 0,
      /* suffix= */ "",
      OfflineMsgProto.class.getName());
  }
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface OfflineMsgOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.vanky.chat.common.protobuf.OfflineMsg)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * </pre>
     *
     * <code>int64 from_user_id = 3;</code>
     * @return The fromUserId.
     */
    long getFromUserId();

    /**
     * <code>int64 goto_timestamp = 2;</code>
     * @return The gotoTimestamp.
     */
    long getGotoTimestamp();

    /**
     * <code>int64 last_timestamp = 5;</code>
     * @return The lastTimestamp.
     */
    long getLastTimestamp();

    /**
     * <code>uint32 msg_count = 1;</code>
     * @return The msgCount.
     */
    int getMsgCount();

    /**
     * <code>int32 chat_type = 6;</code>
     * @return The chatType.
     */
    int getChatType();

    /**
     * <code>string content = 7;</code>
     * @return The content.
     */
    String getContent();
    /**
     * <code>string content = 7;</code>
     * @return The bytes for content.
     */
    com.google.protobuf.ByteString
        getContentBytes();

    /**
     * <pre>
     * </pre>
     *
     * <code>int32 msg_type = 8;</code>
     * @return The msgType.
     */
    int getMsgType();

    /**
     * <code>int32 status = 9;</code>
     * @return The status.
     */
    int getStatus();
  }
  /**
   * Protobuf type {@code com.vanky.chat.common.protobuf.OfflineMsg}
   */
  public static final class OfflineMsg extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:com.vanky.chat.common.protobuf.OfflineMsg)
      OfflineMsgOrBuilder {
  private static final long serialVersionUID = 0L;
    static {
      com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
        com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
        /* major= */ 4,
        /* minor= */ 26,
        /* patch= */ 0,
        /* suffix= */ "",
        OfflineMsg.class.getName());
    }
    // Use OfflineMsg.newBuilder() to construct.
    private OfflineMsg(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
    }
    private OfflineMsg() {
      content_ = "";
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return internal_static_com_vanky_chat_common_protobuf_OfflineMsg_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return internal_static_com_vanky_chat_common_protobuf_OfflineMsg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg.class, com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg.Builder.class);
    }

    public static final int FROM_USER_ID_FIELD_NUMBER = 3;
    private long fromUserId_ = 0L;
    /**
     * <pre>
     * </pre>
     *
     * <code>int64 from_user_id = 3;</code>
     * @return The fromUserId.
     */
    @Override
    public long getFromUserId() {
      return fromUserId_;
    }

    public static final int GOTO_TIMESTAMP_FIELD_NUMBER = 2;
    private long gotoTimestamp_ = 0L;
    /**
     * <code>int64 goto_timestamp = 2;</code>
     * @return The gotoTimestamp.
     */
    @Override
    public long getGotoTimestamp() {
      return gotoTimestamp_;
    }

    public static final int LAST_TIMESTAMP_FIELD_NUMBER = 5;
    private long lastTimestamp_ = 0L;
    /**
     * <code>int64 last_timestamp = 5;</code>
     * @return The lastTimestamp.
     */
    @Override
    public long getLastTimestamp() {
      return lastTimestamp_;
    }

    public static final int MSG_COUNT_FIELD_NUMBER = 1;
    private int msgCount_ = 0;
    /**
     * <code>uint32 msg_count = 1;</code>
     * @return The msgCount.
     */
    @Override
    public int getMsgCount() {
      return msgCount_;
    }

    public static final int CHAT_TYPE_FIELD_NUMBER = 6;
    private int chatType_ = 0;
    /**
     * <code>int32 chat_type = 6;</code>
     * @return The chatType.
     */
    @Override
    public int getChatType() {
      return chatType_;
    }

    public static final int CONTENT_FIELD_NUMBER = 7;
    @SuppressWarnings("serial")
    private volatile Object content_ = "";
    /**
     * <code>string content = 7;</code>
     * @return The content.
     */
    @Override
    public String getContent() {
      Object ref = content_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        content_ = s;
        return s;
      }
    }
    /**
     * <code>string content = 7;</code>
     * @return The bytes for content.
     */
    @Override
    public com.google.protobuf.ByteString
        getContentBytes() {
      Object ref = content_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        content_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int MSG_TYPE_FIELD_NUMBER = 8;
    private int msgType_ = 0;
    /**
     * <pre>
     * </pre>
     *
     * <code>int32 msg_type = 8;</code>
     * @return The msgType.
     */
    @Override
    public int getMsgType() {
      return msgType_;
    }

    public static final int STATUS_FIELD_NUMBER = 9;
    private int status_ = 0;
    /**
     * <code>int32 status = 9;</code>
     * @return The status.
     */
    @Override
    public int getStatus() {
      return status_;
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (msgCount_ != 0) {
        output.writeUInt32(1, msgCount_);
      }
      if (gotoTimestamp_ != 0L) {
        output.writeInt64(2, gotoTimestamp_);
      }
      if (fromUserId_ != 0L) {
        output.writeInt64(3, fromUserId_);
      }
      if (lastTimestamp_ != 0L) {
        output.writeInt64(5, lastTimestamp_);
      }
      if (chatType_ != 0) {
        output.writeInt32(6, chatType_);
      }
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(content_)) {
        com.google.protobuf.GeneratedMessage.writeString(output, 7, content_);
      }
      if (msgType_ != 0) {
        output.writeInt32(8, msgType_);
      }
      if (status_ != 0) {
        output.writeInt32(9, status_);
      }
      getUnknownFields().writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (msgCount_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, msgCount_);
      }
      if (gotoTimestamp_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, gotoTimestamp_);
      }
      if (fromUserId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, fromUserId_);
      }
      if (lastTimestamp_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(5, lastTimestamp_);
      }
      if (chatType_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(6, chatType_);
      }
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(content_)) {
        size += com.google.protobuf.GeneratedMessage.computeStringSize(7, content_);
      }
      if (msgType_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(8, msgType_);
      }
      if (status_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(9, status_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg)) {
        return super.equals(obj);
      }
      com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg other = (com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg) obj;

      if (getFromUserId()
          != other.getFromUserId()) return false;
      if (getGotoTimestamp()
          != other.getGotoTimestamp()) return false;
      if (getLastTimestamp()
          != other.getLastTimestamp()) return false;
      if (getMsgCount()
          != other.getMsgCount()) return false;
      if (getChatType()
          != other.getChatType()) return false;
      if (!getContent()
          .equals(other.getContent())) return false;
      if (getMsgType()
          != other.getMsgType()) return false;
      if (getStatus()
          != other.getStatus()) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + FROM_USER_ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getFromUserId());
      hash = (37 * hash) + GOTO_TIMESTAMP_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getGotoTimestamp());
      hash = (37 * hash) + LAST_TIMESTAMP_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getLastTimestamp());
      hash = (37 * hash) + MSG_COUNT_FIELD_NUMBER;
      hash = (53 * hash) + getMsgCount();
      hash = (37 * hash) + CHAT_TYPE_FIELD_NUMBER;
      hash = (53 * hash) + getChatType();
      hash = (37 * hash) + CONTENT_FIELD_NUMBER;
      hash = (53 * hash) + getContent().hashCode();
      hash = (37 * hash) + MSG_TYPE_FIELD_NUMBER;
      hash = (53 * hash) + getMsgType();
      hash = (37 * hash) + STATUS_FIELD_NUMBER;
      hash = (53 * hash) + getStatus();
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input);
    }
    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseDelimitedWithIOException(PARSER, input);
    }

    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input);
    }
    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.vanky.chat.common.protobuf.OfflineMsg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.vanky.chat.common.protobuf.OfflineMsg)
        com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return internal_static_com_vanky_chat_common_protobuf_OfflineMsg_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return internal_static_com_vanky_chat_common_protobuf_OfflineMsg_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg.class, com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg.Builder.class);
      }

      // Construct using com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg.newBuilder()
      private Builder() {

      }

      private Builder(
          BuilderParent parent) {
        super(parent);

      }
      @Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        fromUserId_ = 0L;
        gotoTimestamp_ = 0L;
        lastTimestamp_ = 0L;
        msgCount_ = 0;
        chatType_ = 0;
        content_ = "";
        msgType_ = 0;
        status_ = 0;
        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return internal_static_com_vanky_chat_common_protobuf_OfflineMsg_descriptor;
      }

      @Override
      public com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg getDefaultInstanceForType() {
        return getDefaultInstance();
      }

      @Override
      public com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg build() {
        com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg buildPartial() {
        com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg result = new com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg(this);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartial0(com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.fromUserId_ = fromUserId_;
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.gotoTimestamp_ = gotoTimestamp_;
        }
        if (((from_bitField0_ & 0x00000004) != 0)) {
          result.lastTimestamp_ = lastTimestamp_;
        }
        if (((from_bitField0_ & 0x00000008) != 0)) {
          result.msgCount_ = msgCount_;
        }
        if (((from_bitField0_ & 0x00000010) != 0)) {
          result.chatType_ = chatType_;
        }
        if (((from_bitField0_ & 0x00000020) != 0)) {
          result.content_ = content_;
        }
        if (((from_bitField0_ & 0x00000040) != 0)) {
          result.msgType_ = msgType_;
        }
        if (((from_bitField0_ & 0x00000080) != 0)) {
          result.status_ = status_;
        }
      }

      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg) {
          return mergeFrom((com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg other) {
        if (other == getDefaultInstance()) return this;
        if (other.getFromUserId() != 0L) {
          setFromUserId(other.getFromUserId());
        }
        if (other.getGotoTimestamp() != 0L) {
          setGotoTimestamp(other.getGotoTimestamp());
        }
        if (other.getLastTimestamp() != 0L) {
          setLastTimestamp(other.getLastTimestamp());
        }
        if (other.getMsgCount() != 0) {
          setMsgCount(other.getMsgCount());
        }
        if (other.getChatType() != 0) {
          setChatType(other.getChatType());
        }
        if (!other.getContent().isEmpty()) {
          content_ = other.content_;
          bitField0_ |= 0x00000020;
          onChanged();
        }
        if (other.getMsgType() != 0) {
          setMsgType(other.getMsgType());
        }
        if (other.getStatus() != 0) {
          setStatus(other.getStatus());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 8: {
                msgCount_ = input.readUInt32();
                bitField0_ |= 0x00000008;
                break;
              } // case 8
              case 16: {
                gotoTimestamp_ = input.readInt64();
                bitField0_ |= 0x00000002;
                break;
              } // case 16
              case 24: {
                fromUserId_ = input.readInt64();
                bitField0_ |= 0x00000001;
                break;
              } // case 24
              case 40: {
                lastTimestamp_ = input.readInt64();
                bitField0_ |= 0x00000004;
                break;
              } // case 40
              case 48: {
                chatType_ = input.readInt32();
                bitField0_ |= 0x00000010;
                break;
              } // case 48
              case 58: {
                content_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000020;
                break;
              } // case 58
              case 64: {
                msgType_ = input.readInt32();
                bitField0_ |= 0x00000040;
                break;
              } // case 64
              case 72: {
                status_ = input.readInt32();
                bitField0_ |= 0x00000080;
                break;
              } // case 72
              default: {
                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                  done = true; // was an endgroup tag
                }
                break;
              } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }
      private int bitField0_;

      private long fromUserId_ ;
      /**
       * <pre>
       * </pre>
       *
       * <code>int64 from_user_id = 3;</code>
       * @return The fromUserId.
       */
      @Override
      public long getFromUserId() {
        return fromUserId_;
      }
      /**
       * <pre>
       * </pre>
       *
       * <code>int64 from_user_id = 3;</code>
       * @param value The fromUserId to set.
       * @return This builder for chaining.
       */
      public Builder setFromUserId(long value) {

        fromUserId_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * </pre>
       *
       * <code>int64 from_user_id = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearFromUserId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        fromUserId_ = 0L;
        onChanged();
        return this;
      }

      private long gotoTimestamp_ ;
      /**
       * <code>int64 goto_timestamp = 2;</code>
       * @return The gotoTimestamp.
       */
      @Override
      public long getGotoTimestamp() {
        return gotoTimestamp_;
      }
      /**
       * <code>int64 goto_timestamp = 2;</code>
       * @param value The gotoTimestamp to set.
       * @return This builder for chaining.
       */
      public Builder setGotoTimestamp(long value) {

        gotoTimestamp_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       * <code>int64 goto_timestamp = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearGotoTimestamp() {
        bitField0_ = (bitField0_ & ~0x00000002);
        gotoTimestamp_ = 0L;
        onChanged();
        return this;
      }

      private long lastTimestamp_ ;
      /**
       * <code>int64 last_timestamp = 5;</code>
       * @return The lastTimestamp.
       */
      @Override
      public long getLastTimestamp() {
        return lastTimestamp_;
      }
      /**
       * <code>int64 last_timestamp = 5;</code>
       * @param value The lastTimestamp to set.
       * @return This builder for chaining.
       */
      public Builder setLastTimestamp(long value) {

        lastTimestamp_ = value;
        bitField0_ |= 0x00000004;
        onChanged();
        return this;
      }
      /**
       * <code>int64 last_timestamp = 5;</code>
       * @return This builder for chaining.
       */
      public Builder clearLastTimestamp() {
        bitField0_ = (bitField0_ & ~0x00000004);
        lastTimestamp_ = 0L;
        onChanged();
        return this;
      }

      private int msgCount_ ;
      /**
       * <code>uint32 msg_count = 1;</code>
       * @return The msgCount.
       */
      @Override
      public int getMsgCount() {
        return msgCount_;
      }
      /**
       * <code>uint32 msg_count = 1;</code>
       * @param value The msgCount to set.
       * @return This builder for chaining.
       */
      public Builder setMsgCount(int value) {

        msgCount_ = value;
        bitField0_ |= 0x00000008;
        onChanged();
        return this;
      }
      /**
       * <code>uint32 msg_count = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearMsgCount() {
        bitField0_ = (bitField0_ & ~0x00000008);
        msgCount_ = 0;
        onChanged();
        return this;
      }

      private int chatType_ ;
      /**
       * <code>int32 chat_type = 6;</code>
       * @return The chatType.
       */
      @Override
      public int getChatType() {
        return chatType_;
      }
      /**
       * <code>int32 chat_type = 6;</code>
       * @param value The chatType to set.
       * @return This builder for chaining.
       */
      public Builder setChatType(int value) {

        chatType_ = value;
        bitField0_ |= 0x00000010;
        onChanged();
        return this;
      }
      /**
       * <code>int32 chat_type = 6;</code>
       * @return This builder for chaining.
       */
      public Builder clearChatType() {
        bitField0_ = (bitField0_ & ~0x00000010);
        chatType_ = 0;
        onChanged();
        return this;
      }

      private Object content_ = "";
      /**
       * <code>string content = 7;</code>
       * @return The content.
       */
      public String getContent() {
        Object ref = content_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          content_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string content = 7;</code>
       * @return The bytes for content.
       */
      public com.google.protobuf.ByteString
          getContentBytes() {
        Object ref = content_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          content_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string content = 7;</code>
       * @param value The content to set.
       * @return This builder for chaining.
       */
      public Builder setContent(
          String value) {
        if (value == null) { throw new NullPointerException(); }
        content_ = value;
        bitField0_ |= 0x00000020;
        onChanged();
        return this;
      }
      /**
       * <code>string content = 7;</code>
       * @return This builder for chaining.
       */
      public Builder clearContent() {
        content_ = getDefaultInstance().getContent();
        bitField0_ = (bitField0_ & ~0x00000020);
        onChanged();
        return this;
      }
      /**
       * <code>string content = 7;</code>
       * @param value The bytes for content to set.
       * @return This builder for chaining.
       */
      public Builder setContentBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        content_ = value;
        bitField0_ |= 0x00000020;
        onChanged();
        return this;
      }

      private int msgType_ ;
      /**
       * <pre>
       * </pre>
       *
       * <code>int32 msg_type = 8;</code>
       * @return The msgType.
       */
      @Override
      public int getMsgType() {
        return msgType_;
      }
      /**
       * <pre>
       * </pre>
       *
       * <code>int32 msg_type = 8;</code>
       * @param value The msgType to set.
       * @return This builder for chaining.
       */
      public Builder setMsgType(int value) {

        msgType_ = value;
        bitField0_ |= 0x00000040;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * </pre>
       *
       * <code>int32 msg_type = 8;</code>
       * @return This builder for chaining.
       */
      public Builder clearMsgType() {
        bitField0_ = (bitField0_ & ~0x00000040);
        msgType_ = 0;
        onChanged();
        return this;
      }

      private int status_ ;
      /**
       * <code>int32 status = 9;</code>
       * @return The status.
       */
      @Override
      public int getStatus() {
        return status_;
      }
      /**
       * <code>int32 status = 9;</code>
       * @param value The status to set.
       * @return This builder for chaining.
       */
      public Builder setStatus(int value) {

        status_ = value;
        bitField0_ |= 0x00000080;
        onChanged();
        return this;
      }
      /**
       * <code>int32 status = 9;</code>
       * @return This builder for chaining.
       */
      public Builder clearStatus() {
        bitField0_ = (bitField0_ & ~0x00000080);
        status_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.vanky.chat.common.protobuf.OfflineMsg)
    }

    // @@protoc_insertion_point(class_scope:com.vanky.chat.common.protobuf.OfflineMsg)
    private static final com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg();
    }

    public static com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<OfflineMsg>
        PARSER = new com.google.protobuf.AbstractParser<OfflineMsg>() {
      @Override
      public OfflineMsg parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        Builder builder = newBuilder();
        try {
          builder.mergeFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(builder.buildPartial());
        } catch (com.google.protobuf.UninitializedMessageException e) {
          throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(e)
              .setUnfinishedMessage(builder.buildPartial());
        }
        return builder.buildPartial();
      }
    };

    public static com.google.protobuf.Parser<OfflineMsg> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<OfflineMsg> getParserForType() {
      return PARSER;
    }

    @Override
    public com.vanky.chat.common.protobuf.OfflineMsgProto.OfflineMsg getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_vanky_chat_common_protobuf_OfflineMsg_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_com_vanky_chat_common_protobuf_OfflineMsg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\025OfflineMsgProto.proto\022\036com.vanky.chat." +
      "common.protobuf\"\253\001\n\nOfflineMsg\022\024\n\014from_u" +
      "ser_id\030\003 \001(\003\022\026\n\016goto_timestamp\030\002 \001(\003\022\026\n\016" +
      "last_timestamp\030\005 \001(\003\022\021\n\tmsg_count\030\001 \001(\r\022" +
      "\021\n\tchat_type\030\006 \001(\005\022\017\n\007content\030\007 \001(\t\022\020\n\010m" +
      "sg_type\030\010 \001(\005\022\016\n\006status\030\t \001(\005B1\n\036com.van" +
      "ky.chat.common.protobufB\017OfflineMsgProto" +
      "b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_com_vanky_chat_common_protobuf_OfflineMsg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_vanky_chat_common_protobuf_OfflineMsg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_com_vanky_chat_common_protobuf_OfflineMsg_descriptor,
        new String[] { "FromUserId", "GotoTimestamp", "LastTimestamp", "MsgCount", "ChatType", "Content", "MsgType", "Status", });
    descriptor.resolveAllFeaturesImmutable();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
