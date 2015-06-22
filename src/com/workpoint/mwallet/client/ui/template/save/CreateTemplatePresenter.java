package com.workpoint.mwallet.client.ui.template.save;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.workpoint.mwallet.client.service.TaskServiceCallback;
import com.workpoint.mwallet.client.ui.MainPagePresenter;
import com.workpoint.mwallet.client.ui.events.ActivitySavedEvent;
import com.workpoint.mwallet.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.mwallet.client.ui.events.ProcessingEvent;
import com.workpoint.mwallet.shared.model.TemplateDTO;
import com.workpoint.mwallet.shared.requests.SaveTemplateRequest;
import com.workpoint.mwallet.shared.responses.SaveTemplateResponse;

public class CreateTemplatePresenter extends
		PresenterWidget<CreateTemplatePresenter.MyView> {

	public interface MyView extends View {
		boolean isValid();

		void setTemplate(TemplateDTO templateSelected);

		TemplateDTO getTemplateDTO();

		String getTemplateType();

		String getTemplateName();

		int getTemplateDefault();

		String getTemplateTill();

		String getTemplateText();

	}

	@Inject
	DispatchAsync requestHelper;

	private TemplateDTO selected;

	@Inject
	MainPagePresenter mainPagePresenter;

	@Inject
	public CreateTemplatePresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);

	}

	@Override
	protected void onBind() {
		super.onBind();

	}

	public void submitData() {

		fireEvent(new ProcessingEvent());

		fireEvent(new ProcessingEvent("Saving ..."));

		TemplateDTO template = new TemplateDTO();

		String message = getView().getTemplateText();

		template.setMessage(message);
		template.setType(getView().getTemplateType());
		template.setName(getView().getTemplateName());
		template.setTillModel_Id(getView().getTemplateTill());
		template.setIsDefault(getView().getTemplateDefault());

		SaveTemplateRequest saveRequest = new SaveTemplateRequest(template,
				false);
		requestHelper.execute(saveRequest,
				new TaskServiceCallback<SaveTemplateResponse>() {
					@Override
					public void processResult(SaveTemplateResponse aResponse) {
						fireEvent(new ProcessingCompletedEvent());
						fireEvent(new ActivitySavedEvent("Template "
								+ " successfully saved"));
					}
				});

	}

	public void setTemplateDetails(TemplateDTO selected) {
		this.selected = selected;
		getView().setTemplate(selected);
	}

}