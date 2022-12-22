package com.coderdojo.zen.award;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AwardModelAssembler implements RepresentationModelAssembler<Award, EntityModel<Award>> {

    @Override
    public EntityModel<Award> toModel(Award award) {

        // Unconditional links to single-item resource and aggregate root

        EntityModel<Award> awardModel = EntityModel.of(award,
                linkTo(methodOn(AwardController.class).one(award.getId())).withSelfRel(),
                linkTo(methodOn(AwardController.class).all()).withRel("awards"));

        // Conditional links based on state of the award

        if (award.getStatus() == Status.IN_PROGRESS) {
            awardModel.add(linkTo(methodOn(AwardController.class).cancel(award.getId())).withRel("cancel"));
            awardModel.add(linkTo(methodOn(AwardController.class).complete(award.getId())).withRel("complete"));
        }

        return awardModel;
    }
}
