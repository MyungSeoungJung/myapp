package com.msj.myapp.post;

import com.msj.myapp.contact.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>{


    Page<Post> findByTitle(String title,Pageable page);

    Page<Post> findByTitleContaining(String title, Pageable page);

    Page<Post> findByTitleContainsOrCreatorNameContains(String title, String creatorName, Pageable pageable);




}
