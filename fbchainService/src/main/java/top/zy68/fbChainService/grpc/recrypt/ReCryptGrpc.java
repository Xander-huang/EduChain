package top.zy68.fbChainService.grpc.recrypt;


import io.grpc.stub.ClientCalls;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.13.1)",
        comments = "Source: recrypt.proto")
public final class ReCryptGrpc {

    private ReCryptGrpc() {}

    public static final String SERVICE_NAME = "recrypt.reCrypt";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<ReCrypt.generateKeysRequest,
            ReCrypt.generateKeysResponse> getGenerateKeysMethod;

    public static io.grpc.MethodDescriptor<ReCrypt.generateKeysRequest,
            ReCrypt.generateKeysResponse> getGenerateKeysMethod() {
        io.grpc.MethodDescriptor<ReCrypt.generateKeysRequest, ReCrypt.generateKeysResponse> getGenerateKeysMethod;
        if ((getGenerateKeysMethod = ReCryptGrpc.getGenerateKeysMethod) == null) {
            synchronized (ReCryptGrpc.class) {
                if ((getGenerateKeysMethod = ReCryptGrpc.getGenerateKeysMethod) == null) {
                    ReCryptGrpc.getGenerateKeysMethod = getGenerateKeysMethod =
                            io.grpc.MethodDescriptor.<ReCrypt.generateKeysRequest, ReCrypt.generateKeysResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(
                                            "recrypt.reCrypt", "GenerateKeys"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.generateKeysRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.generateKeysResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new reCryptMethodDescriptorSupplier("GenerateKeys"))
                                    .build();
                }
            }
        }
        return getGenerateKeysMethod;
    }

    private static volatile io.grpc.MethodDescriptor<ReCrypt.ACryptRequest,
            ReCrypt.ACryptResponse> getCryptTextMethod;

    public static io.grpc.MethodDescriptor<ReCrypt.ACryptRequest,
            ReCrypt.ACryptResponse> getCryptTextMethod() {
        io.grpc.MethodDescriptor<ReCrypt.ACryptRequest, ReCrypt.ACryptResponse> getCryptTextMethod;
        if ((getCryptTextMethod = ReCryptGrpc.getCryptTextMethod) == null) {
            synchronized (ReCryptGrpc.class) {
                if ((getCryptTextMethod = ReCryptGrpc.getCryptTextMethod) == null) {
                    ReCryptGrpc.getCryptTextMethod = getCryptTextMethod =
                            io.grpc.MethodDescriptor.<ReCrypt.ACryptRequest, ReCrypt.ACryptResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(
                                            "recrypt.reCrypt", "CryptText"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.ACryptRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.ACryptResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new reCryptMethodDescriptorSupplier("CryptText"))
                                    .build();
                }
            }
        }
        return getCryptTextMethod;
    }

    private static volatile io.grpc.MethodDescriptor<ReCrypt.ReCryptRequest,
            ReCrypt.ReCryptResponse> getReCryptMethod;

    public static io.grpc.MethodDescriptor<ReCrypt.ReCryptRequest,
            ReCrypt.ReCryptResponse> getReCryptMethod() {
        io.grpc.MethodDescriptor<ReCrypt.ReCryptRequest, ReCrypt.ReCryptResponse> getReCryptMethod;
        if ((getReCryptMethod = ReCryptGrpc.getReCryptMethod) == null) {
            synchronized (ReCryptGrpc.class) {
                if ((getReCryptMethod = ReCryptGrpc.getReCryptMethod) == null) {
                    ReCryptGrpc.getReCryptMethod = getReCryptMethod =
                            io.grpc.MethodDescriptor.<ReCrypt.ReCryptRequest, ReCrypt.ReCryptResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(
                                            "recrypt.reCrypt", "ReCrypt"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.ReCryptRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.ReCryptResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new reCryptMethodDescriptorSupplier("ReCrypt"))
                                    .build();
                }
            }
        }
        return getReCryptMethod;
    }

    private static volatile io.grpc.MethodDescriptor<ReCrypt.DeCryptRequest,
            ReCrypt.DeCryptResponse> getDeCryptMethod;

    public static io.grpc.MethodDescriptor<ReCrypt.DeCryptRequest,
            ReCrypt.DeCryptResponse> getDeCryptMethod() {
        io.grpc.MethodDescriptor<ReCrypt.DeCryptRequest, ReCrypt.DeCryptResponse> getDeCryptMethod;
        if ((getDeCryptMethod = ReCryptGrpc.getDeCryptMethod) == null) {
            synchronized (ReCryptGrpc.class) {
                if ((getDeCryptMethod = ReCryptGrpc.getDeCryptMethod) == null) {
                    ReCryptGrpc.getDeCryptMethod = getDeCryptMethod =
                            io.grpc.MethodDescriptor.<ReCrypt.DeCryptRequest, ReCrypt.DeCryptResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(
                                            "recrypt.reCrypt", "DeCrypt"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.DeCryptRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.DeCryptResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new reCryptMethodDescriptorSupplier("DeCrypt"))
                                    .build();
                }
            }
        }
        return getDeCryptMethod;
    }

    private static volatile io.grpc.MethodDescriptor<ReCrypt.DeCryptOwnRequest,
            ReCrypt.DeCryptOwnResponse> getDeCryptByOwnPriMethod;

    public static io.grpc.MethodDescriptor<ReCrypt.DeCryptOwnRequest,
            ReCrypt.DeCryptOwnResponse> getDeCryptByOwnPriMethod() {
        io.grpc.MethodDescriptor<ReCrypt.DeCryptOwnRequest, ReCrypt.DeCryptOwnResponse> getDeCryptByOwnPriMethod;
        if ((getDeCryptByOwnPriMethod = ReCryptGrpc.getDeCryptByOwnPriMethod) == null) {
            synchronized (ReCryptGrpc.class) {
                if ((getDeCryptByOwnPriMethod = ReCryptGrpc.getDeCryptByOwnPriMethod) == null) {
                    ReCryptGrpc.getDeCryptByOwnPriMethod = getDeCryptByOwnPriMethod =
                            io.grpc.MethodDescriptor.<ReCrypt.DeCryptOwnRequest, ReCrypt.DeCryptOwnResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(
                                            "recrypt.reCrypt", "DeCryptByOwnPri"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.DeCryptOwnRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            ReCrypt.DeCryptOwnResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new reCryptMethodDescriptorSupplier("DeCryptByOwnPri"))
                                    .build();
                }
            }
        }
        return getDeCryptByOwnPriMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static reCryptStub newStub(io.grpc.Channel channel) {
        return new reCryptStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static reCryptBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        return new reCryptBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static reCryptFutureStub newFutureStub(
            io.grpc.Channel channel) {
        return new reCryptFutureStub(channel);
    }

    /**
     */
    public static abstract class reCryptImplBase implements io.grpc.BindableService {

        /**
         * <pre>
         * 接口 1 生成公私钥
         * </pre>
         */
        public void generateKeys(ReCrypt.generateKeysRequest request,
                                 io.grpc.stub.StreamObserver<ReCrypt.generateKeysResponse> responseObserver) {
            asyncUnimplementedUnaryCall(getGenerateKeysMethod(), responseObserver);
        }

        /**
         * <pre>
         * 接口 2 加密明文产生密文
         * </pre>
         */
        public void cryptText(ReCrypt.ACryptRequest request,
                              io.grpc.stub.StreamObserver<ReCrypt.ACryptResponse> responseObserver) {
            asyncUnimplementedUnaryCall(getCryptTextMethod(), responseObserver);
        }

        /**
         * <pre>
         * 接口 3 server重加密
         * </pre>
         */
        public void reCrypt(ReCrypt.ReCryptRequest request,
                            io.grpc.stub.StreamObserver<ReCrypt.ReCryptResponse> responseObserver) {
            asyncUnimplementedUnaryCall(getReCryptMethod(), responseObserver);
        }

        /**
         * <pre>
         *接口 4 B解密密文得到明文
         * </pre>
         */
        public void deCrypt(ReCrypt.DeCryptRequest request,
                            io.grpc.stub.StreamObserver<ReCrypt.DeCryptResponse> responseObserver) {
            asyncUnimplementedUnaryCall(getDeCryptMethod(), responseObserver);
        }

        /**
         * <pre>
         *接口 4 A通过自己的私钥来解密密文，得到明文
         * </pre>
         */
        public void deCryptByOwnPri(ReCrypt.DeCryptOwnRequest request,
                                    io.grpc.stub.StreamObserver<ReCrypt.DeCryptOwnResponse> responseObserver) {
            asyncUnimplementedUnaryCall(getDeCryptByOwnPriMethod(), responseObserver);
        }

        @Override public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            getGenerateKeysMethod(),
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            ReCrypt.generateKeysRequest,
                                            ReCrypt.generateKeysResponse>(
                                            this, METHODID_GENERATE_KEYS)))
                    .addMethod(
                            getCryptTextMethod(),
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            ReCrypt.ACryptRequest,
                                            ReCrypt.ACryptResponse>(
                                            this, METHODID_CRYPT_TEXT)))
                    .addMethod(
                            getReCryptMethod(),
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            ReCrypt.ReCryptRequest,
                                            ReCrypt.ReCryptResponse>(
                                            this, METHODID_RE_CRYPT)))
                    .addMethod(
                            getDeCryptMethod(),
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            ReCrypt.DeCryptRequest,
                                            ReCrypt.DeCryptResponse>(
                                            this, METHODID_DE_CRYPT)))
                    .addMethod(
                            getDeCryptByOwnPriMethod(),
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            ReCrypt.DeCryptOwnRequest,
                                            ReCrypt.DeCryptOwnResponse>(
                                            this, METHODID_DE_CRYPT_BY_OWN_PRI)))
                    .build();
        }
    }

    /**
     */
    public static final class reCryptStub extends io.grpc.stub.AbstractStub<reCryptStub> {
        private reCryptStub(io.grpc.Channel channel) {
            super(channel);
        }

        private reCryptStub(io.grpc.Channel channel,
                            io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected reCryptStub build(io.grpc.Channel channel,
                                    io.grpc.CallOptions callOptions) {
            return new reCryptStub(channel, callOptions);
        }

        /**
         * <pre>
         * 接口 1 生成公私钥
         * </pre>
         */
        public void generateKeys(ReCrypt.generateKeysRequest request,
                                 io.grpc.stub.StreamObserver<ReCrypt.generateKeysResponse> responseObserver) {
            ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getGenerateKeysMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         * <pre>
         * 接口 2 加密明文产生密文
         * </pre>
         */
        public void cryptText(ReCrypt.ACryptRequest request,
                              io.grpc.stub.StreamObserver<ReCrypt.ACryptResponse> responseObserver) {
            ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getCryptTextMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         * <pre>
         * 接口 3 server重加密
         * </pre>
         */
        public void reCrypt(ReCrypt.ReCryptRequest request,
                            io.grpc.stub.StreamObserver<ReCrypt.ReCryptResponse> responseObserver) {
            ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getReCryptMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         * <pre>
         *接口 4 B解密密文得到明文
         * </pre>
         */
        public void deCrypt(ReCrypt.DeCryptRequest request,
                            io.grpc.stub.StreamObserver<ReCrypt.DeCryptResponse> responseObserver) {
            ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getDeCryptMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         * <pre>
         *接口 4 A通过自己的私钥来解密密文，得到明文
         * </pre>
         */
        public void deCryptByOwnPri(ReCrypt.DeCryptOwnRequest request,
                                    io.grpc.stub.StreamObserver<ReCrypt.DeCryptOwnResponse> responseObserver) {
            ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getDeCryptByOwnPriMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     */
    public static final class reCryptBlockingStub extends io.grpc.stub.AbstractStub<reCryptBlockingStub> {
        private reCryptBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private reCryptBlockingStub(io.grpc.Channel channel,
                                    io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected reCryptBlockingStub build(io.grpc.Channel channel,
                                            io.grpc.CallOptions callOptions) {
            return new reCryptBlockingStub(channel, callOptions);
        }

        /**
         * <pre>
         * 接口 1 生成公私钥
         * </pre>
         */
        public ReCrypt.generateKeysResponse generateKeys(ReCrypt.generateKeysRequest request) {
            return blockingUnaryCall(
                    getChannel(), getGenerateKeysMethod(), getCallOptions(), request);
        }

        /**
         * <pre>
         * 接口 2 加密明文产生密文
         * </pre>
         */
        public ReCrypt.ACryptResponse cryptText(ReCrypt.ACryptRequest request) {
            return blockingUnaryCall(
                    getChannel(), getCryptTextMethod(), getCallOptions(), request);
        }

        /**
         * <pre>
         * 接口 3 server重加密
         * </pre>
         */
        public ReCrypt.ReCryptResponse reCrypt(ReCrypt.ReCryptRequest request) {
            return blockingUnaryCall(
                    getChannel(), getReCryptMethod(), getCallOptions(), request);
        }

        /**
         * <pre>
         *接口 4 B解密密文得到明文
         * </pre>
         */
        public ReCrypt.DeCryptResponse deCrypt(ReCrypt.DeCryptRequest request) {
            return blockingUnaryCall(
                    getChannel(), getDeCryptMethod(), getCallOptions(), request);
        }

        /**
         * <pre>
         *接口 4 A通过自己的私钥来解密密文，得到明文
         * </pre>
         */
        public ReCrypt.DeCryptOwnResponse deCryptByOwnPri(ReCrypt.DeCryptOwnRequest request) {
            return blockingUnaryCall(
                    getChannel(), getDeCryptByOwnPriMethod(), getCallOptions(), request);
        }
    }

    /**
     */
    public static final class reCryptFutureStub extends io.grpc.stub.AbstractStub<reCryptFutureStub> {
        private reCryptFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private reCryptFutureStub(io.grpc.Channel channel,
                                  io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected reCryptFutureStub build(io.grpc.Channel channel,
                                          io.grpc.CallOptions callOptions) {
            return new reCryptFutureStub(channel, callOptions);
        }

        /**
         * <pre>
         * 接口 1 生成公私钥
         * </pre>
         */
        public com.google.common.util.concurrent.ListenableFuture<ReCrypt.generateKeysResponse> generateKeys(
                ReCrypt.generateKeysRequest request) {
            return futureUnaryCall(
                    getChannel().newCall(getGenerateKeysMethod(), getCallOptions()), request);
        }

        /**
         * <pre>
         * 接口 2 加密明文产生密文
         * </pre>
         */
        public com.google.common.util.concurrent.ListenableFuture<ReCrypt.ACryptResponse> cryptText(
                ReCrypt.ACryptRequest request) {
            return futureUnaryCall(
                    getChannel().newCall(getCryptTextMethod(), getCallOptions()), request);
        }

        /**
         * <pre>
         * 接口 3 server重加密
         * </pre>
         */
        public com.google.common.util.concurrent.ListenableFuture<ReCrypt.ReCryptResponse> reCrypt(
                ReCrypt.ReCryptRequest request) {
            return futureUnaryCall(
                    getChannel().newCall(getReCryptMethod(), getCallOptions()), request);
        }

        /**
         * <pre>
         *接口 4 B解密密文得到明文
         * </pre>
         */
        public com.google.common.util.concurrent.ListenableFuture<ReCrypt.DeCryptResponse> deCrypt(
                ReCrypt.DeCryptRequest request) {
            return futureUnaryCall(
                    getChannel().newCall(getDeCryptMethod(), getCallOptions()), request);
        }

        /**
         * <pre>
         *接口 4 A通过自己的私钥来解密密文，得到明文
         * </pre>
         */
        public com.google.common.util.concurrent.ListenableFuture<ReCrypt.DeCryptOwnResponse> deCryptByOwnPri(
                ReCrypt.DeCryptOwnRequest request) {
            return futureUnaryCall(
                    getChannel().newCall(getDeCryptByOwnPriMethod(), getCallOptions()), request);
        }
    }

    private static final int METHODID_GENERATE_KEYS = 0;
    private static final int METHODID_CRYPT_TEXT = 1;
    private static final int METHODID_RE_CRYPT = 2;
    private static final int METHODID_DE_CRYPT = 3;
    private static final int METHODID_DE_CRYPT_BY_OWN_PRI = 4;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final reCryptImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(reCryptImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_GENERATE_KEYS:
                    serviceImpl.generateKeys((ReCrypt.generateKeysRequest) request,
                            (io.grpc.stub.StreamObserver<ReCrypt.generateKeysResponse>) responseObserver);
                    break;
                case METHODID_CRYPT_TEXT:
                    serviceImpl.cryptText((ReCrypt.ACryptRequest) request,
                            (io.grpc.stub.StreamObserver<ReCrypt.ACryptResponse>) responseObserver);
                    break;
                case METHODID_RE_CRYPT:
                    serviceImpl.reCrypt((ReCrypt.ReCryptRequest) request,
                            (io.grpc.stub.StreamObserver<ReCrypt.ReCryptResponse>) responseObserver);
                    break;
                case METHODID_DE_CRYPT:
                    serviceImpl.deCrypt((ReCrypt.DeCryptRequest) request,
                            (io.grpc.stub.StreamObserver<ReCrypt.DeCryptResponse>) responseObserver);
                    break;
                case METHODID_DE_CRYPT_BY_OWN_PRI:
                    serviceImpl.deCryptByOwnPri((ReCrypt.DeCryptOwnRequest) request,
                            (io.grpc.stub.StreamObserver<ReCrypt.DeCryptOwnResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    private static abstract class reCryptBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        reCryptBaseDescriptorSupplier() {}

        @Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return ReCrypt.getDescriptor();
        }

        @Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("reCrypt");
        }
    }

    private static final class reCryptFileDescriptorSupplier
            extends reCryptBaseDescriptorSupplier {
        reCryptFileDescriptorSupplier() {}
    }

    private static final class reCryptMethodDescriptorSupplier
            extends reCryptBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        reCryptMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (ReCryptGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new reCryptFileDescriptorSupplier())
                            .addMethod(getGenerateKeysMethod())
                            .addMethod(getCryptTextMethod())
                            .addMethod(getReCryptMethod())
                            .addMethod(getDeCryptMethod())
                            .addMethod(getDeCryptByOwnPriMethod())
                            .build();
                }
            }
        }
        return result;
    }
}
