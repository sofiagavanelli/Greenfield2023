package com.example.chat;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: CommunicationService.proto")
public final class CommunicationServiceGrpc {

  private CommunicationServiceGrpc() {}

  public static final String SERVICE_NAME = "com.example.chat.CommunicationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Goodbye,
      com.google.protobuf.Empty> getRemovalMsgMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "removalMsg",
      requestType = com.example.chat.CommunicationServiceOuterClass.Goodbye.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Goodbye,
      com.google.protobuf.Empty> getRemovalMsgMethod() {
    io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Goodbye, com.google.protobuf.Empty> getRemovalMsgMethod;
    if ((getRemovalMsgMethod = CommunicationServiceGrpc.getRemovalMsgMethod) == null) {
      synchronized (CommunicationServiceGrpc.class) {
        if ((getRemovalMsgMethod = CommunicationServiceGrpc.getRemovalMsgMethod) == null) {
          CommunicationServiceGrpc.getRemovalMsgMethod = getRemovalMsgMethod =
              io.grpc.MethodDescriptor.<com.example.chat.CommunicationServiceOuterClass.Goodbye, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "removalMsg"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.chat.CommunicationServiceOuterClass.Goodbye.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("removalMsg"))
              .build();
        }
      }
    }
    return getRemovalMsgMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Presentation,
      com.google.protobuf.Empty> getPresentationMsgMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "presentationMsg",
      requestType = com.example.chat.CommunicationServiceOuterClass.Presentation.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Presentation,
      com.google.protobuf.Empty> getPresentationMsgMethod() {
    io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Presentation, com.google.protobuf.Empty> getPresentationMsgMethod;
    if ((getPresentationMsgMethod = CommunicationServiceGrpc.getPresentationMsgMethod) == null) {
      synchronized (CommunicationServiceGrpc.class) {
        if ((getPresentationMsgMethod = CommunicationServiceGrpc.getPresentationMsgMethod) == null) {
          CommunicationServiceGrpc.getPresentationMsgMethod = getPresentationMsgMethod =
              io.grpc.MethodDescriptor.<com.example.chat.CommunicationServiceOuterClass.Presentation, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "presentationMsg"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.chat.CommunicationServiceOuterClass.Presentation.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("presentationMsg"))
              .build();
        }
      }
    }
    return getPresentationMsgMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Request,
      com.example.chat.CommunicationServiceOuterClass.Authorization> getRequestMechanicMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "requestMechanic",
      requestType = com.example.chat.CommunicationServiceOuterClass.Request.class,
      responseType = com.example.chat.CommunicationServiceOuterClass.Authorization.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Request,
      com.example.chat.CommunicationServiceOuterClass.Authorization> getRequestMechanicMethod() {
    io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Request, com.example.chat.CommunicationServiceOuterClass.Authorization> getRequestMechanicMethod;
    if ((getRequestMechanicMethod = CommunicationServiceGrpc.getRequestMechanicMethod) == null) {
      synchronized (CommunicationServiceGrpc.class) {
        if ((getRequestMechanicMethod = CommunicationServiceGrpc.getRequestMechanicMethod) == null) {
          CommunicationServiceGrpc.getRequestMechanicMethod = getRequestMechanicMethod =
              io.grpc.MethodDescriptor.<com.example.chat.CommunicationServiceOuterClass.Request, com.example.chat.CommunicationServiceOuterClass.Authorization>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "requestMechanic"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.chat.CommunicationServiceOuterClass.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.chat.CommunicationServiceOuterClass.Authorization.getDefaultInstance()))
              .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("requestMechanic"))
              .build();
        }
      }
    }
    return getRequestMechanicMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Authorization,
      com.google.protobuf.Empty> getAnswerPendingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "answerPending",
      requestType = com.example.chat.CommunicationServiceOuterClass.Authorization.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Authorization,
      com.google.protobuf.Empty> getAnswerPendingMethod() {
    io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.Authorization, com.google.protobuf.Empty> getAnswerPendingMethod;
    if ((getAnswerPendingMethod = CommunicationServiceGrpc.getAnswerPendingMethod) == null) {
      synchronized (CommunicationServiceGrpc.class) {
        if ((getAnswerPendingMethod = CommunicationServiceGrpc.getAnswerPendingMethod) == null) {
          CommunicationServiceGrpc.getAnswerPendingMethod = getAnswerPendingMethod =
              io.grpc.MethodDescriptor.<com.example.chat.CommunicationServiceOuterClass.Authorization, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "answerPending"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.chat.CommunicationServiceOuterClass.Authorization.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("answerPending"))
              .build();
        }
      }
    }
    return getAnswerPendingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash,
      com.google.protobuf.Empty> getOrganizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "organize",
      requestType = com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash,
      com.google.protobuf.Empty> getOrganizeMethod() {
    io.grpc.MethodDescriptor<com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash, com.google.protobuf.Empty> getOrganizeMethod;
    if ((getOrganizeMethod = CommunicationServiceGrpc.getOrganizeMethod) == null) {
      synchronized (CommunicationServiceGrpc.class) {
        if ((getOrganizeMethod = CommunicationServiceGrpc.getOrganizeMethod) == null) {
          CommunicationServiceGrpc.getOrganizeMethod = getOrganizeMethod =
              io.grpc.MethodDescriptor.<com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "organize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("organize"))
              .build();
        }
      }
    }
    return getOrganizeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CommunicationServiceStub newStub(io.grpc.Channel channel) {
    return new CommunicationServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CommunicationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CommunicationServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CommunicationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CommunicationServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class CommunicationServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void removalMsg(com.example.chat.CommunicationServiceOuterClass.Goodbye request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getRemovalMsgMethod(), responseObserver);
    }

    /**
     */
    public void presentationMsg(com.example.chat.CommunicationServiceOuterClass.Presentation request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getPresentationMsgMethod(), responseObserver);
    }

    /**
     */
    public void requestMechanic(com.example.chat.CommunicationServiceOuterClass.Request request,
        io.grpc.stub.StreamObserver<com.example.chat.CommunicationServiceOuterClass.Authorization> responseObserver) {
      asyncUnimplementedUnaryCall(getRequestMechanicMethod(), responseObserver);
    }

    /**
     */
    public void answerPending(com.example.chat.CommunicationServiceOuterClass.Authorization request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getAnswerPendingMethod(), responseObserver);
    }

    /**
     */
    public void organize(com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getOrganizeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRemovalMsgMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.chat.CommunicationServiceOuterClass.Goodbye,
                com.google.protobuf.Empty>(
                  this, METHODID_REMOVAL_MSG)))
          .addMethod(
            getPresentationMsgMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.chat.CommunicationServiceOuterClass.Presentation,
                com.google.protobuf.Empty>(
                  this, METHODID_PRESENTATION_MSG)))
          .addMethod(
            getRequestMechanicMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.chat.CommunicationServiceOuterClass.Request,
                com.example.chat.CommunicationServiceOuterClass.Authorization>(
                  this, METHODID_REQUEST_MECHANIC)))
          .addMethod(
            getAnswerPendingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.chat.CommunicationServiceOuterClass.Authorization,
                com.google.protobuf.Empty>(
                  this, METHODID_ANSWER_PENDING)))
          .addMethod(
            getOrganizeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash,
                com.google.protobuf.Empty>(
                  this, METHODID_ORGANIZE)))
          .build();
    }
  }

  /**
   */
  public static final class CommunicationServiceStub extends io.grpc.stub.AbstractStub<CommunicationServiceStub> {
    private CommunicationServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CommunicationServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommunicationServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CommunicationServiceStub(channel, callOptions);
    }

    /**
     */
    public void removalMsg(com.example.chat.CommunicationServiceOuterClass.Goodbye request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemovalMsgMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void presentationMsg(com.example.chat.CommunicationServiceOuterClass.Presentation request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPresentationMsgMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void requestMechanic(com.example.chat.CommunicationServiceOuterClass.Request request,
        io.grpc.stub.StreamObserver<com.example.chat.CommunicationServiceOuterClass.Authorization> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRequestMechanicMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void answerPending(com.example.chat.CommunicationServiceOuterClass.Authorization request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAnswerPendingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void organize(com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getOrganizeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CommunicationServiceBlockingStub extends io.grpc.stub.AbstractStub<CommunicationServiceBlockingStub> {
    private CommunicationServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CommunicationServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommunicationServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CommunicationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.google.protobuf.Empty removalMsg(com.example.chat.CommunicationServiceOuterClass.Goodbye request) {
      return blockingUnaryCall(
          getChannel(), getRemovalMsgMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty presentationMsg(com.example.chat.CommunicationServiceOuterClass.Presentation request) {
      return blockingUnaryCall(
          getChannel(), getPresentationMsgMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.chat.CommunicationServiceOuterClass.Authorization requestMechanic(com.example.chat.CommunicationServiceOuterClass.Request request) {
      return blockingUnaryCall(
          getChannel(), getRequestMechanicMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty answerPending(com.example.chat.CommunicationServiceOuterClass.Authorization request) {
      return blockingUnaryCall(
          getChannel(), getAnswerPendingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty organize(com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash request) {
      return blockingUnaryCall(
          getChannel(), getOrganizeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CommunicationServiceFutureStub extends io.grpc.stub.AbstractStub<CommunicationServiceFutureStub> {
    private CommunicationServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CommunicationServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommunicationServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CommunicationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> removalMsg(
        com.example.chat.CommunicationServiceOuterClass.Goodbye request) {
      return futureUnaryCall(
          getChannel().newCall(getRemovalMsgMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> presentationMsg(
        com.example.chat.CommunicationServiceOuterClass.Presentation request) {
      return futureUnaryCall(
          getChannel().newCall(getPresentationMsgMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.chat.CommunicationServiceOuterClass.Authorization> requestMechanic(
        com.example.chat.CommunicationServiceOuterClass.Request request) {
      return futureUnaryCall(
          getChannel().newCall(getRequestMechanicMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> answerPending(
        com.example.chat.CommunicationServiceOuterClass.Authorization request) {
      return futureUnaryCall(
          getChannel().newCall(getAnswerPendingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> organize(
        com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash request) {
      return futureUnaryCall(
          getChannel().newCall(getOrganizeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REMOVAL_MSG = 0;
  private static final int METHODID_PRESENTATION_MSG = 1;
  private static final int METHODID_REQUEST_MECHANIC = 2;
  private static final int METHODID_ANSWER_PENDING = 3;
  private static final int METHODID_ORGANIZE = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CommunicationServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CommunicationServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REMOVAL_MSG:
          serviceImpl.removalMsg((com.example.chat.CommunicationServiceOuterClass.Goodbye) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_PRESENTATION_MSG:
          serviceImpl.presentationMsg((com.example.chat.CommunicationServiceOuterClass.Presentation) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_REQUEST_MECHANIC:
          serviceImpl.requestMechanic((com.example.chat.CommunicationServiceOuterClass.Request) request,
              (io.grpc.stub.StreamObserver<com.example.chat.CommunicationServiceOuterClass.Authorization>) responseObserver);
          break;
        case METHODID_ANSWER_PENDING:
          serviceImpl.answerPending((com.example.chat.CommunicationServiceOuterClass.Authorization) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_ORGANIZE:
          serviceImpl.organize((com.example.chat.CommunicationServiceOuterClass.UncontrolledCrash) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CommunicationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CommunicationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.chat.CommunicationServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CommunicationService");
    }
  }

  private static final class CommunicationServiceFileDescriptorSupplier
      extends CommunicationServiceBaseDescriptorSupplier {
    CommunicationServiceFileDescriptorSupplier() {}
  }

  private static final class CommunicationServiceMethodDescriptorSupplier
      extends CommunicationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CommunicationServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CommunicationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CommunicationServiceFileDescriptorSupplier())
              .addMethod(getRemovalMsgMethod())
              .addMethod(getPresentationMsgMethod())
              .addMethod(getRequestMechanicMethod())
              .addMethod(getAnswerPendingMethod())
              .addMethod(getOrganizeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
