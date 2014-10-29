package com.workpoint.mwallet.client.ui.admin.users.save;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.workpoint.mwallet.client.service.TaskServiceCallback;
import com.workpoint.mwallet.client.ui.events.LoadGroupsEvent;
import com.workpoint.mwallet.client.ui.events.LoadUsersEvent;
import com.workpoint.mwallet.shared.model.HTUser;
import com.workpoint.mwallet.shared.model.UserGroup;
import com.workpoint.mwallet.shared.requests.GetGroupsRequest;
import com.workpoint.mwallet.shared.requests.SaveGroupRequest;
import com.workpoint.mwallet.shared.requests.SaveUserRequest;
import com.workpoint.mwallet.shared.responses.GetGroupsResponse;
import com.workpoint.mwallet.shared.responses.SaveGroupResponse;
import com.workpoint.mwallet.shared.responses.SaveUserResponse;

public class UserSavePresenter extends PresenterWidget<UserSavePresenter.IUserSaveView> {

	public interface IUserSaveView extends PopupView {

		void setType(TYPE type);
		
		HasClickHandlers getSaveUser();
		
		HasClickHandlers getSaveGroup();

		boolean isValid();

		HTUser getUser();

		void setUser(HTUser user);

		UserGroup getGroup();

		void setGroup(UserGroup group);

		void setGroups(List<UserGroup> groups);
		
	}

	public enum TYPE{
		GROUP, USER
	}
	
	TYPE type;
	
	HTUser user;
	
	UserGroup group;
	
	@Inject DispatchAsync requestHelper;
	
	@Inject
	public UserSavePresenter(final EventBus eventBus, final IUserSaveView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getSaveUser().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					HTUser htuser = getView().getUser();
					if(user!=null){
						htuser.setId(user.getId());
					}
					SaveUserRequest request = new SaveUserRequest(htuser);
					requestHelper.execute(request, new TaskServiceCallback<SaveUserResponse>() {
						@Override
						public void processResult(SaveUserResponse result) {
							user = result.getUser();
							getView().setUser(user);
							getView().hide();
							fireEvent(new LoadUsersEvent());
						}
					});
				}
			}
		});
		
		getView().getSaveGroup().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					UserGroup userGroup = getView().getGroup();				
					
					SaveGroupRequest request = new SaveGroupRequest(userGroup);
					
					requestHelper.execute(request, new TaskServiceCallback<SaveGroupResponse>() {
						@Override
						public void processResult(SaveGroupResponse result) {
							group = result.getGroup();
							getView().setGroup(group);
							fireEvent(new LoadGroupsEvent());
							getView().hide();
						}
					});
				}
			}
		});
	}
	
	
	@Override
	protected void onReveal() {
		super.onReveal();
		
		GetGroupsRequest request = new GetGroupsRequest();
		requestHelper.execute(request, new TaskServiceCallback<GetGroupsResponse>() {
			@Override
			public void processResult(GetGroupsResponse result) {
				List<UserGroup> groups = result.getGroups();
				getView().setGroups(groups);
			}
		});
	}
	
	public void setType(TYPE type, Object value){
		this.type = type;
		getView().setType(type);
		if(value!=null){
			if(type==TYPE.USER){
				user= (HTUser)value;
				getView().setUser(user);
			}else{
				group= (UserGroup)value;
				getView().setGroup(group);
			}
		}
		
	}
}
