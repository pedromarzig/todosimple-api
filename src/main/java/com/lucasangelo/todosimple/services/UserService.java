package com.lucasangelo.todosimple.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasangelo.todosimple.models.User;
import com.lucasangelo.todosimple.repositories.UserRepository;
import com.lucasangelo.todosimple.services.exceptions.DataBindingViolationException;
import com.lucasangelo.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    


    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
            "Usuário não encontrado! id" + id + ", Tipo: " + User.class.getName()
        ));
    }

    @Transactional
    public User createUser(User obj){
        obj.setId(null);
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj){
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);
    }
    
    public void delete(Long id){
        findById(id);
        try{
            this.userRepository.deleteById(id);
        }catch(Exception e){
            throw new DataBindingViolationException(
                "Não é possivel excluir pois há entedidades relacionadas!"
            );
        }
    }

}
