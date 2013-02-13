package org.kubek2k.springockito.annotations.internal.factory;

import org.kubek2k.springockito.annotations.internal.ResettableMock;
import org.mockito.Answers;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.springframework.beans.factory.FactoryBean;


public class MockFactoryBean<T> implements FactoryBean<T>, ResettableMock {

    private Class<T> mockClass;
    private final Class[] extraInterfaces;
    private final String mockName;
    private final Answers defaultAnswer;
    private T instance;

    public MockFactoryBean(Class<T> mockClass, Class[] extraInterfaces, String mockName, Answers defaultAnswer) {
        this.mockClass = mockClass;
        this.extraInterfaces = extraInterfaces;
        this.mockName = mockName;
        this.defaultAnswer = defaultAnswer;
    }

    public Class<? extends T> getObjectType() {
        return mockClass;
    }

    public boolean isSingleton() {
        return true;
    }

    public T getObject() throws Exception {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    private T createInstance() {
        MockSettings mockSettings = new MockSettingsImpl();

        if (extraInterfaces.length > 0) {
            mockSettings = mockSettings.extraInterfaces(extraInterfaces);
        }

        if (defaultAnswer != null) {
            mockSettings = mockSettings.defaultAnswer(defaultAnswer.get());
        }

        if (mockName != null) {
            mockSettings = mockSettings.name(mockName);
        }

        return Mockito.mock(mockClass, mockSettings);
    }

    public void resetMock() {
        T object = null;
        try {
            object = getObject();
            Mockito.reset(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}