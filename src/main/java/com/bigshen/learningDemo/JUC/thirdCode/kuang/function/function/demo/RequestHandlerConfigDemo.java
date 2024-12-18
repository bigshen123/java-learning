package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.function.demo;

import com.bigshen.learningDemo.common.model.BaseResponse;

import java.util.UUID;
import java.util.function.Function;

/**
 * @author byj
 * @date 2024/12/18
 * @Description 对Function接口做封装 分别调用其核心方法：
 *  apply 方法接收一个输入参数，并将其传递给函数进行处理，最终返回一个响应结果
 *  compose：将一个函数组合到另一个函数的前面（即先执行组合的函数，然后再执行原函数）。
 *  andThen：将一个函数组合到另一个函数的后面（即先执行原函数，然后再执行组合的函数）。
 *  identity：返回一个对输入值执行原样返回的函数，通常用于不进行任何修改的情况。
 */
public class RequestHandlerConfigDemo {
    public static void main(String[] args) {
        // 创建处理登录请求的函数
        Function<LoginRequest, BaseResponse<LoginResponse>> loginHandler = request -> {
            // 假设简单的登录逻辑
            String message = "Welcome, " + request.getUsername() + "!";
            return new BaseResponse<>(UUID.randomUUID().toString(), "v1.0", new LoginResponse(message));
        };

        // 创建处理用户信息请求的函数
        Function<UserInfoRequest, BaseResponse<UserInfoResponse>> userInfoHandler = request -> {
            // 假设简单的查询用户信息逻辑
            String username = "User" + request.getUserId();
            String email = "user" + request.getUserId() + "@example.com";
            return new BaseResponse<>(UUID.randomUUID().toString(), "v1.0", new UserInfoResponse(username, email));
        };

        // 配置登录请求的处理器
        RequestHandlerConfig<LoginRequest, LoginResponse> loginHandlerConfig = new RequestHandlerConfig<>(
                loginHandler, LoginRequest.class, LoginResponse.class
        );

        // 配置用户信息请求的处理器
        RequestHandlerConfig<UserInfoRequest, UserInfoResponse> userInfoHandlerConfig = new RequestHandlerConfig<>(
                userInfoHandler, UserInfoRequest.class, UserInfoResponse.class
        );

        // 模拟请求处理
        // apply 方法接收一个输入参数，并将其传递给函数进行处理，最终返回一个响应结果
        LoginRequest loginRequest = new LoginRequest("john_doe", "password123");
        BaseResponse<LoginResponse> loginResponse = loginHandlerConfig.getHandler().apply(loginRequest);
        // Login Response: Welcome, john_doe!
        System.out.println("Login Response: " + loginResponse.getData().getMessage());

        UserInfoRequest userInfoRequest = new UserInfoRequest(1);
        BaseResponse<UserInfoResponse> userInfoResponse = userInfoHandlerConfig.getHandler().apply(userInfoRequest);
        // User Info Response: User1 | user1@example.com
        System.out.println("User Info Response: " + userInfoResponse.getData().getUsername() + " | " +
                userInfoResponse.getData().getEmail());

        // compose: 先执行 addPrefix，再执行 loginHandler
        // compose：将一个函数组合到另一个函数的前面（即先执行组合的函数，然后再执行原函数）。
        Function<LoginRequest, BaseResponse<LoginResponse>> composeTest = loginHandler.compose(addPrefixToUsername());
        BaseResponse<LoginResponse> composedResponse = composeTest.apply(new LoginRequest("john_doe", "password123"));
        // Composed Response (with prefix): Welcome, Mr. john_doe!
        System.out.println("Composed Response (with prefix): " + composedResponse.getData().getMessage());

        // andThen: 先执行 loginHandler，再执行 addSuffix
        // andThen：将一个函数组合到另一个函数的后面（即先执行原函数，然后再执行组合的函数）。
        Function<LoginRequest, BaseResponse<LoginResponse>> andThenTest = loginHandler.andThen(addSuffixToMessage());
        BaseResponse<LoginResponse> andThenResponse = andThenTest.apply(new LoginRequest("john_doe", "password123"));
        // AndThen Response (with suffix): Welcome, john_doe! - Have a great day!
        System.out.println("AndThen Response (with suffix): " + andThenResponse.getData().getMessage());

        // identity: 直接返回原始输入，不做任何更改
        // identity：返回一个对输入值执行原样返回的函数，通常用于不进行任何修改的情况。
        Function<LoginRequest, LoginRequest> identityTest = Function.identity();
        LoginRequest identityResult = identityTest.apply(new LoginRequest("john_doe", "password123"));
        // Identity Test: john_doe
        System.out.println("Identity Test: " + identityResult.getUsername());
    }

    // 组合一个前缀，添加到用户名
    private static Function<LoginRequest, LoginRequest> addPrefixToUsername() {
        return request -> {
            String modifiedUsername = "Mr. " + request.getUsername();
            return new LoginRequest(modifiedUsername, request.getPassword());
        };
    }

    // 添加后缀到登录响应消息
    private static Function<BaseResponse<LoginResponse>, BaseResponse<LoginResponse>> addSuffixToMessage() {
        return response -> {
            String modifiedMessage = response.getData().getMessage() + " - Have a great day!";
            return new BaseResponse<>(response.getId(), response.getApiVersion(), new LoginResponse(modifiedMessage));
        };
    }
}
