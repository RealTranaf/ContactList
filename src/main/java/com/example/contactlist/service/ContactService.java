package com.example.contactlist.service;

import com.example.contactlist.model.Contact;
import com.example.contactlist.repository.ContactRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.example.contactlist.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {
    @Autowired
    private ContactRepo contactRepo;

    public Page<Contact> getAllContact(int page, int size){
        return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }
    public Contact getContact(String contactId){
        return contactRepo.findById(contactId).orElseThrow(() -> new RuntimeException("Contact not found!"));
    }
    public Contact createContact(Contact contact){
        return contactRepo.save(contact);
    }

    public void deleteContact(String contactId){
        contactRepo.deleteById(contactId);
    }
    public String uploadPhoto(String contactId, MultipartFile image){
        Contact contact = getContact(contactId);
        String photoUrl = photoFunction(contactId, image);
        contact.setPhotoUrl(photoUrl);
        contactRepo.save(contact);
        return photoUrl;
    }

    private String fileExtension(String fileName){
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ".png"; // empty extension
        }
        String extension = "." + fileName.substring(lastIndexOf + 1);
        return extension;
    }

    private String photoFunction(String contactId, MultipartFile image){ //save photo to local and get photourl
        String extension = fileExtension(image.getOriginalFilename());
        try{
            Path filePath = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(filePath)){
                Files.createDirectories(filePath);
            }
            Files.copy(image.getInputStream(), filePath.resolve(contactId + extension), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("contacts/image/" + contactId + extension).toUriString();
        } catch (Exception e){
            throw new RuntimeException("Unable to save image!");
        }
    }

//    private void savePhotoToLocal(String contactId, MultipartFile image){
//        String extension = fileExtension(image.getOriginalFilename());
//        try{
//            Path filePath = Paths.get("").toAbsolutePath().normalize();
//            if(!Files.exists(filePath)){
//                Files.createDirectories(filePath);
//            }
//            Files.copy(image.getInputStream(), filePath.resolve(contactId + extension), REPLACE_EXISTING);
//        } catch (Exception e){
//            throw new RuntimeException("Unable to save image!");
//        }
//    }
}
