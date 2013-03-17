JqueryFileuploadRailsExample::Application.routes.draw do

  get "gallery/view"

  get "gallery/edit"

  resources :uploads
  match ':controller(/:action(/:id))(.:format)'
  root :to => 'static_pages#home'
  
  match '/help', 	to: 'static_pages#help'
  match '/create', 	to: 'static_pages#create'
  match '/home',	to: 'static_pages#home'
  match '/upload',  to: 'uploads#index'
  match '/signup',  to: 'users#new'
  match '/login',	to: 'sessions#login'
  match '/logout',  to: 'sessions#logout'
  match '/profile',  to: 'sessions#profile'
  match '/settings', to: 'sessions#setting'
  match '/sessions/home', to: 'static_pages#home'

end
