package com.bigshen.learningDemo.design.builder.ResourcePoolBuilder;

/**
 * @Author BYJ
 * @Date 2022/5/6 16:41
 * @Describe 在下面的 ConstructorArg 类中，当 isRef 为 true 的时候，arg 表示 String 类型的 refBeanId，type 不需要设置；
 * 当 isRef 为 false 的时候，arg、type 都需要设置。请根据这个需求，完善 ConstructorArg 类。
 */
public class ConstructorArg {
    private boolean isRef;
    private Class type;
    private Object arg;

    public boolean isRef() {
        return isRef;
    }

    public Class getType() {
        return type;
    }

    public Object getArg() {
        return arg;
    }

    private ConstructorArg(Builder builder) {
        this.isRef = builder.isRef;
        this.type = builder.type;
        this.arg = builder.arg;
    }

    public static class Builder {
        private boolean isRef;
        private Class type;
        private Object arg;

        public ConstructorArg build() {
            if (isRef && type != null) {
                throw new IllegalArgumentException("...");
            }
            if (!isRef && type == null) {
                throw new IllegalArgumentException("...");
            }
            if (this.isRef && (arg != null && arg.getClass() != String.class)) {
                throw new IllegalArgumentException("...");
            }

            if (!this.isRef && arg == null) {
                throw new IllegalArgumentException("...");
            }

            return new ConstructorArg(this);
        }

        public Builder setRef(boolean ref) {
            if(ref && this.type != null) {
                throw new IllegalArgumentException("...");
            }
            this.isRef = ref;
            return this;
        }

        public Builder setType(Class type) {
            if (this.isRef || type == null) {
                throw new IllegalArgumentException("...");
            }
            this.type = type;
            return this;
        }

        public Builder setArg(Object arg) {
            if (this.isRef && (arg != null && arg.getClass() != String.class)) {
                throw new IllegalArgumentException("...");
            }

            if (!this.isRef && arg == null) {
                throw new IllegalArgumentException("...");
            }
            this.arg = arg;
            return this;
        }
    }

}
