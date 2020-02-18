package com.example.kashif.android.di.components;


import com.example.kashif.android.di.ConfigPersistent;
import com.example.kashif.android.di.modules.ActivityModule;

import dagger.Component;

@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {
    ActivityComponent activityComponent(ActivityModule activityModule);
}
