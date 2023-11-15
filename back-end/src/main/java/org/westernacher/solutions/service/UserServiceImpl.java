package org.westernacher.solutions.service;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.westernacher.solutions.domain.entities.SavedOrder;
import org.westernacher.solutions.domain.entities.User;
import org.westernacher.solutions.domain.entities.UserRole;
import org.westernacher.solutions.domain.models.binding.SavedOrderBindingModel;
import org.westernacher.solutions.domain.models.binding.UserEditBindingModel;
import org.westernacher.solutions.domain.models.binding.UserRoleFactory;
import org.westernacher.solutions.domain.models.service.SavedOrderServiceModel;
import org.westernacher.solutions.domain.models.service.UserServiceModel;
import org.westernacher.solutions.repository.OrdersRepository;
import org.westernacher.solutions.repository.RoleRepository;
import org.westernacher.solutions.repository.SavedOrdersRepository;
import org.westernacher.solutions.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleService userRoleService;
    private final UserRoleFactory userRoleFactory;
    private final SavedOrdersRepository savedOrdersRepository;
    private final OrdersRepository ordersRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder, UserRoleService userRoleService, UserRoleFactory userRoleFactory, SavedOrdersRepository savedOrdersRepository, OrdersRepository ordersRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRoleService = userRoleService;
        this.userRoleFactory = userRoleFactory;
        this.savedOrdersRepository = savedOrdersRepository;
        this.ordersRepository = ordersRepository;
    }

    private Set<UserRole> getAuthorities(String authority)
    {
        Set<UserRole> userAuthorities = new HashSet<>();

        userAuthorities.add(this.roleRepository.getByAuthority(authority));

        return userAuthorities;
    }

    @Override
    public String getUserAuthority(String userId)
    {

        Set<UserRole> authorities;

        authorities =
                        this.userRepository
                        .findById(userId)
                        .get()
                        .getAuthorities();

        String authority = null;

        if(authorities.stream().filter(o -> o.getAuthority().equals("ROOT-ADMIN")).findFirst().isPresent())
        {
            authority = "ROOT-ADMIN";
        }
        else if(authorities.stream().filter(o -> o.getAuthority().equals("ADMIN")).findFirst().isPresent())
        {
            authority = "ADMIN";
        }
        else if(authorities.stream().filter(o -> o.getAuthority().equals("MODERATOR")).findFirst().isPresent())
        {
            authority = "MODERATOR";
        }
        else if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_USER")).findFirst().isPresent())
        {
            authority = "ROLE_USER";
        }

        System.out.println("Curr: " + authority);
        return authority;
    }

    @Override
    public boolean createUser(UserServiceModel userServiceModel)
    {
        User userEntity = this.modelMapper.map(userServiceModel, User.class);

        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

        Set<UserRole> authorities = new HashSet<>();

        if(this.userRepository.findAll().isEmpty())
        {
            authorities.add(userRoleFactory.createUserRole("ROOT-ADMIN"));
            authorities.add(userRoleFactory.createUserRole("ADMIN"));
            authorities.add(userRoleFactory.createUserRole("MODERATOR"));
            authorities.add(userRoleFactory.createUserRole("ROLE_USER"));
        }
        else
        {
            authorities.add(this.userRoleService.findRole("ROLE_USER"));
        }

        userEntity.setAuthorities(authorities);

        try
        {
            this.userRepository.save(userEntity);
            return true;
        }
        catch (DataIntegrityViolationException e)
        {
            System.out.println("Username already exist");
            return false;
        }
    }

    @Override
    public boolean editUser(UserEditBindingModel userEditBindingModel)
    {
        Optional<User> foundUser = this.userRepository.findByUsername(userEditBindingModel.getUsername());

        if(foundUser.isPresent())
        {
            foundUser.get().setFirstName(userEditBindingModel.getFirstName());
            foundUser.get().setLastName(userEditBindingModel.getLastName());
            foundUser.get().setEmail(userEditBindingModel.getEmail());
            foundUser.get().setBirthDate(userEditBindingModel.getBirthDate());

//            String firstName = foundUser.get().getFirstName();
//            firstName = "PEtranka"; // This does not
//
//            Date date = foundUser.get().getBirthDate();
//            date.setTime(34);           // This do the things

            this.userRepository.save(foundUser.get());

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean deleteUser(String username) {
        User user = (User)loadUserByUsername(username);
        user.setAuthorities(null);

        try
        {
            this.userRepository.delete(user);
        }
        catch (DataIntegrityViolationException dataIntegrityViolationException)
        {
            return false;
        }

        return true;
    }

    @Override
    public Set<UserServiceModel> getAll()
    {
        return this.userRepository
                .findAll()
                .stream()
                .map(x -> this.modelMapper.map(x, UserServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean promoteUser(String id)
    {
        User user = this.userRepository
                .findById(id)
                .orElse(null);

        if(user == null) return false;

        String userAuthority = this.getUserAuthority(user.getId());
        System.out.println("BEFORE PROMOTE USER AUTHORITY: " + userAuthority);
        Set<UserRole> authorities = new HashSet<>();

        switch (userAuthority)
        {
            case "ROLE_USER":
                authorities.add(userRoleService.findRole("MODERATOR"));
                authorities.add(userRoleService.findRole("ROLE_USER"));
                user.setAuthorities(authorities);
//                user.setAuthorities(this.getAuthorities("MODERATOR"));
                break;
            case "MODERATOR":
                authorities.add(userRoleService.findRole("ADMIN"));
                authorities.add(userRoleService.findRole("MODERATOR"));
                authorities.add(userRoleService.findRole("ROLE_USER"));
                user.setAuthorities(authorities);
//                user.setAuthorities(this.getAuthorities("ADMIN"));
                break;
            default:
                throw new IllegalArgumentException("There is no role, higher than ADMIN");
        }
        System.out.println("AFTER PROMOTE USER AUTHORITY: " + this.getUserAuthority(user.getId()));
        this.userRepository.save(user);
        return true;
    }

    @Override
    public boolean demoteUser(String id)
    {
        User user = this.userRepository
                .findById(id)
                .orElse(null);

        if(user == null) return false;

        String userAuthority = this.getUserAuthority(user.getId());
        System.out.println("USER BEFORE DEMOTE AUTHORITY: " + userAuthority);
        Set<UserRole> authorities = new HashSet<>();

        switch (userAuthority)
        {
//            case "ROOT-ADMIN":
//                authorities.add(userRoleService.findRole("ADMIN"));
//                authorities.add(userRoleService.findRole("MODERATOR"));
//                authorities.add(userRoleService.findRole("ROLE_USER"));
//                user.setAuthorities(authorities);
////                user.setAuthorities(this.getAuthorities("ADMIN"));
//                break;
            case "ADMIN":
                authorities.add(userRoleService.findRole("MODERATOR"));
                authorities.add(userRoleService.findRole("ROLE_USER"));
                user.setAuthorities(authorities);
//                user.setAuthorities(this.getAuthorities("MODERATOR"));
                break;
            case "MODERATOR":
                authorities.add(userRoleService.findRole("ROLE_USER"));
                user.setAuthorities(authorities);
//                user.setAuthorities(this.getAuthorities("ROLE_USER"));
                break;
            default:
                throw new IllegalArgumentException("There is no role, lower than ROLE_USER");
        }
        System.out.println("AFTER DEMOTE USER AUTHORITY: " + this.getUserAuthority(user.getId()));
        this.userRepository.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = this.userRepository
                .findByUsername(username)
                .orElse(null);

        if(user == null)
        {
            throw new UsernameNotFoundException("No such user.");
        }

        return user;
    }

    @Override
    public List<SavedOrder> getUserCart(String username)
    {
        Optional<User> user = this.userRepository.findByUsername(username);

        if(user.isPresent())
        {
            return user.get().getCart();
        }
        else
        {
            throw new NullPointerException("Empty cart");
        }
    }

    @Override
    public int getUserCartSize(String username)
    {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isPresent())
        {
            try
            {
                return this.savedOrdersRepository.countAllByUser(user.get());
            }
            catch(DataIntegrityViolationException e)
            {
                return 0;
            }
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getWaitingOrdersSize()
    {
        try
        {
           return this.ordersRepository.countAllByDeliveredFalse();
        }
        catch(DataIntegrityViolationException e)
        {
           return 0;
        }
    }

    @Override
    public int getDeliveredOrdersSize()
    {
        try
        {
            return this.ordersRepository.countAllByDeliveredTrue();
        }
        catch(DataIntegrityViolationException e)
        {
            return 0;
        }
    }

    @Override
    public ResponseEntity removeSavedOrder(String id)
    {
        try
        {
            this.savedOrdersRepository.deleteById(id);
            return ResponseEntity.ok().body(true);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @Override
    public ResponseEntity saveOrder(SavedOrderBindingModel savedOrderBindingModel)
    {
        try
        {
            Optional<User> user = this.userRepository.findByUsername(savedOrderBindingModel.getUsername());
            SavedOrderServiceModel savedOrderServiceModel = new SavedOrderServiceModel();
            modelMapper.map(savedOrderBindingModel, savedOrderServiceModel);
            savedOrderServiceModel.setUser(user.get());
            this.savedOrdersRepository.save(modelMapper.map(savedOrderServiceModel, SavedOrder.class));
            return ResponseEntity.ok().body(true);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
