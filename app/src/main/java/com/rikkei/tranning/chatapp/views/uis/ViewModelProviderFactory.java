package com.rikkei.tranning.chatapp.views.uis;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.rikkei.tranning.chatapp.views.uis.message.ChatViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jyotidubey on 22/02/19.
 */
@Singleton
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

  @Inject
  public ViewModelProviderFactory() {

  }


  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    if (modelClass.isAssignableFrom(ChatViewModel.class)) {
      //noinspection unchecked
      return (T) new ChatViewModel();
    }
    throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
  }
}