/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.kaloyan.snackoverflow.entity.Image;
import tech.kaloyan.snackoverflow.resources.req.ImageReq;
import tech.kaloyan.snackoverflow.resources.resp.ImageResp;

import java.util.List;

@Mapper
public interface ImageMapper {
    ImageMapper MAPPER = Mappers.getMapper(ImageMapper.class);

    ImageResp toImageResp(Image image);

    Image toImage(ImageReq imageReq);

    List<ImageResp> toImageResps(List<Image> imageList);
}
