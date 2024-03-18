package org.fexisaf.flexisafadvencefour.eventListener;

import lombok.Getter;
import lombok.Setter;
import org.fexisaf.flexisafadvencefour.entity.TokenEntity;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ApplicationEventList extends ApplicationEvent {
    private TokenEntity tokenEntity;
    private String url;
    public ApplicationEventList(TokenEntity tokenEntity, String url) {
        super(tokenEntity);
        this.tokenEntity = tokenEntity;
        this.url = url;
    }
}
