package com.netcracker.rest;

import com.netcracker.classes.UserJson;
import com.netcracker.entity.CarEntity;
import com.netcracker.entity.UserAccessLevelEntity;
import com.netcracker.entity.UserEntity;
import com.netcracker.facade.local_int.Car;
import com.netcracker.facade.local_int.User;
import com.netcracker.facade.local_int.UserAccessLevel;
import com.netcracker.rest.utils.SecuritySettings;
import com.netcracker.service.Mail;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Path("user")
public class UserRest {

	@EJB
	User user;
	@EJB
	UserAccessLevel userAccessLevel;
	@EJB
	Car car;

	@POST
	@Path("login")
	@Consumes("application/json")
	public Response getUser(UserJson userJson) {
		UserEntity userEntity = null;
		if (userJson.getCred().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
			userEntity = user.loginByEmail(userJson.getCred(), userJson.getPass());
		} else if (userJson.getCred().matches("\\d+")) {
			userEntity = user.loginByPhone(userJson.getCred().replace("+", "").replace(" ", ""), userJson.getPass());
		}
		if (userEntity != null) {
			return Response.status(200).entity(userEntity.getUuid()).build();
		} else {
			return Response.status(404).entity("Bad login credentials").build();
		}
	}

	@POST
	@Path("getAccessLevelsByUuid")
	@Consumes("text/plain")
	public Response getNextU(String uuid) {
		System.out.println(uuid);
		UserEntity userEntity = user.findByUuid(uuid);
		Collection<UserAccessLevelEntity> userAccessLevels = userEntity.getUserAccessLevelEntities();

		StringBuilder sb = new StringBuilder();
		sb.append("{\"userAccessLevel\":[");
		for (UserAccessLevelEntity userAccessLevel : userAccessLevels) {
			sb.append("{\"id\":\"")
					.append(userAccessLevel.getId())
					.append("\",\"level\":\"")
					.append(userAccessLevel.getName())
					.append("\" },");
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		sb.append("]}");

		if (!userAccessLevels.isEmpty()) {
			return Response.status(200).entity(sb.toString()).build();
		} else {
			return Response.status(404).entity("Bad response.").build();
		}
	}

	@POST
	@Path("create")
	@Consumes("application/json")
	public Response createUser(UserJson userJson) {
		UserEntity userEntity = null;
		String randomUuid = UUID.randomUUID().toString();
		if (!user.isEmailUsed(userJson.getEmail()) && !user.isPhoneUsed(userJson.getPhone())) {
			userEntity = new UserEntity();
			userEntity.setFirstName(userJson.getFirstName());
			userEntity.setLastName(userJson.getLastName());
			userEntity.setPassword(userJson.getPass());
			userEntity.setPhone(userJson.getPhone().replace("+", "").replace(" ", ""));
			userEntity.setEmail(userJson.getEmail());
			userEntity.setDateRegistered(new Timestamp(new Date().getTime()));
			userEntity.setUuid(randomUuid);
			userEntity.setUserAccessLevelEntities(Arrays.asList(userAccessLevel.read(new BigInteger("2"))));
			user.create(userEntity);
			try {
				Mail.sendMail(userJson.getEmail(), "Taxi Service confirmation",
						"http://178.151.17.247/nctc/api/user/confirm?encryptedUuid="
								+ URLEncoder.encode(SecuritySettings.encrypt(randomUuid), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//TODO Replace with real URL
		}
		if (userEntity == null) {
			return Response.status(404).entity("Phone or email is already in use").build();
		} else {
			return Response.status(201).entity(randomUuid).build();
		}
	}

	@POST
	@Path("create_driver")
	@Consumes("application/json")
	public Response cuUser(UserJson userJson) {
		UserEntity userEntity = null;
		if (userJson.getId() == null) {
			userEntity = createUserEntityByUserJson(userJson);
			user.create(userEntity);
			setCar(userJson.getCarId(), userEntity.getId().toString());
		} else {
			userEntity = editUserEntityByJson(userJson);
			setCar(userJson.getCarId(), userJson.getId());
			user.update(userEntity);
		}
		if (userEntity == null) {
			return Response.status(404).entity("Phone or email is already in use").build();
		} else {
			return Response.status(201).entity("user add").build();
		}
	}


	@POST
	@Path("delete")
	@Consumes("application/json")
	public Response deleteUserById(UserJson id) {
		user.delete(new BigInteger(id.getId()));
		if (user.read(new BigInteger(id.getId())) == null) {
			return Response.status(200).entity("User is delete").build();
		} else {
			return Response.status(404).entity("User is not delete").build();
		}
	}

	private UserEntity createUserEntityByUserJson(UserJson userJson) {
		UserEntity userEntity = null;
		String randomUuid = UUID.randomUUID().toString();
		if (!user.isEmailUsed(userJson.getEmail()) && !user.isPhoneUsed(userJson.getPhone())) {
			userEntity = new UserEntity();
			userEntity.setFirstName(userJson.getFirstName());
			userEntity.setLastName(userJson.getLastName());
			userEntity.setPassword(userJson.getPass());
			userEntity.setPhone(userJson.getPhone().replace("+", "").replace(" ", ""));
			userEntity.setEmail(userJson.getEmail());
			userEntity.setDateRegistered(new Timestamp(new Date().getTime()));
			userEntity.setUuid(randomUuid);
			userEntity.setUserAccessLevelEntities(Arrays.asList(userAccessLevel.read(new BigInteger("3"))));
		}
		return userEntity;
	}

	private void setCar(String carId, String userId) {
		CarEntity carEntity = car.read(new BigInteger(carId));
		carEntity.setUserEntity(user.read(new BigInteger(userId)));
		car.update(carEntity);
	}

	private UserEntity editUserEntityByJson(UserJson userJson) {
		UserEntity userEntity = user.read(new BigInteger(userJson.getId()));
		String randomUuid = UUID.randomUUID().toString();
		if (!user.isEmailUsed(userJson.getEmail()) && !user.isPhoneUsed(userJson.getPhone()) ||
				userEntity.getEmail().equals(userJson.getEmail()) ||
				userEntity.getPhone().equals(userJson.getPhone().replace("+", "").replace(" ", ""))) {
			userEntity.setFirstName(userJson.getFirstName());
			userEntity.setLastName(userJson.getLastName());
			userEntity.setPassword(userJson.getPass());
			userEntity.setPhone(userJson.getPhone().replace("+", "").replace(" ", ""));
			userEntity.setEmail(userJson.getEmail());
			userEntity.setDateRegistered(new Timestamp(new Date().getTime()));
			userEntity.setUuid(randomUuid);
			userEntity.setUserAccessLevelEntities(Arrays.asList(userAccessLevel.read(new BigInteger("3"))));
		}
		return userEntity;
	}

	@GET
	@Path("confirm")
	public Response confirm(@QueryParam("encryptedUuid") String encryptedUuid) {
		UserEntity userEntity = user.findByUuid(SecuritySettings.decrypt(encryptedUuid));

		if (userEntity == null) {
			return Response.status(404).entity("Wrong user uuid passed\n" + encryptedUuid).build();
		}
		userEntity.setConfirmed(true);
		user.update(userEntity);
		return Response.status(201).entity("" +
				"<script>" +
				"alert('Email confirmed');" +
				"document.location.href = \"http://178.151.17.247/nctc/customer.html\";" +
				"</script>").build(); //TODO not to GAVNO
	}

	@POST
	@Path("getUserDataByUuid")
	@Consumes("text/plain")
	public Response getUserDataByUUID(String uuid) {
		UserEntity userEntity = user.findByUuid(uuid);

		StringBuilder sb = new StringBuilder();
		sb.append("{\"userData\":[");

		sb.append("{\"name\":\"")
				.append(userEntity.getFirstName())
				.append("\",\"phone\":\"")
				.append(userEntity.getPhone())
				.append("\",\"myMail\":\"")
				.append(userEntity.getEmail())
				.append("\" },");
		sb.replace(sb.length() - 1, sb.length(), "");
		sb.append("]}");
		if (userEntity != null) {
			return Response.status(200).entity(sb.toString()).build();
		} else {
			return Response.status(404).entity("Bad response.").build();
		}
	}


	@POST
	@Path("getUserDataById")
	@Consumes("text/plain")
	public Response getUserDataByID(String id) {
		UserEntity userEntity = user.read(new BigInteger(id));

		StringBuilder sb = new StringBuilder();
		sb.append("{\"userData\":[")
				.append("{\"firstName\":\"")
				.append(userEntity.getFirstName())
				.append("\",\"lastName\":\"")
				.append(userEntity.getLastName())
				.append("\",\"phone\":\"")
				.append(userEntity.getPhone())
				.append("\",\"userId\":\"")
				.append(userEntity.getId())
				.append("\",\"email\":\"")
				.append(userEntity.getEmail())
				.append("\",\"getPassword\":\"")
				.append(userEntity.getPassword())
				.append("\" },");
		sb.replace(sb.length() - 1, sb.length(), "");
		sb.append("]}");
		if (userEntity != null) {
			return Response.status(200).entity(sb.toString()).build();
		} else {
			return Response.status(404).entity("Bad response.").build();
		}
	}


	@POST
	@Path("getAllUserByUUID")
	@Consumes("text/plain")
	public Response getAllUserByUUID(String uuid) {
		UserEntity userEntity = user.findByUuid(uuid);
		StringBuilder sb = new StringBuilder();
		sb.append("{\"userData\":[")
				.append("{\"firstName\":\"")
				.append(userEntity.getFirstName())
				.append("\",\"lastName\":\"")
				.append(userEntity.getLastName())
				.append("\",\"phone\":\"")
				.append(userEntity.getPhone())
				.append("\",\"userId\":\"")
				.append(userEntity.getId())
				.append("\",\"email\":\"")
				.append(userEntity.getEmail())
				.append("\",\"userSex\":\"")
				.append(userEntity.getSex())
				.append("\",\"animalFriendly\":\"")
				.append(userEntity.getAnimalFriendly())
				.append("\",\"smokingFriendly\":\"")
				.append(userEntity.getSmokingFriendly())
				.append("\",\"alternativePhone\":\"")
				.append(userEntity.getAlternativePhone())
				.append("\",\"getPassword\":\"")
				.append(userEntity.getPassword())
				.append("\" },");
		sb.replace(sb.length() - 1, sb.length(), "");
		sb.append("]}");
		if (userEntity != null) {
			return Response.status(200).entity(sb.toString()).build();
		} else {
			return Response.status(404).entity("Bad response.").build();
		}
	}

	@POST
	@Path("editUserByUUID")
	@Consumes("application/json")
	public Response editUser(UserJson userJson) {
		UserEntity userEntity = user.findByUuid(userJson.getUuid());
		if (!user.isEmailUsed(userJson.getEmail()) && !user.isPhoneUsed(userJson.getPhone()) ||
				userEntity.getEmail().equals(userJson.getEmail()) ||
				userEntity.getPhone().equals(userJson.getPhone().replace("+", "").replace(" ", ""))) {
			userEntity.setFirstName(userJson.getFirstName());
			userEntity.setLastName(userJson.getLastName());
			userEntity.setPassword(userJson.getPass());
			userEntity.setPhone(userJson.getPhone().replace("+", "").replace(" ", ""));
			userEntity.setAlternativePhone(userJson.getAlternativePhone());
			userEntity.setEmail(userJson.getEmail());
			userEntity.setSex(userJson.getUserSex());
			userEntity.setSmokingFriendly(new Boolean(userJson.getSmokingFriendly()));
			userEntity.setAnimalFriendly(new Boolean(userJson.getAnimalFriendly()));
			//userEntity.setUserAccessLevelEntities(Arrays.asList(userAccessLevel.read(new BigInteger("1"))));
			user.update(userEntity);
		}
		if (userEntity == null) {
			return Response.status(404).entity("Phone or email is already in use").build();
		} else {
			return Response.status(201).entity("user add").build();
		}
	}

	@POST
	@Path("resUpUser")
	@Consumes("application/json")
	public Response resUpUser(UserJson userJson) {
		UserEntity userEntity = user.read(new BigInteger(userJson.getId()));
		userEntity.setFirstName(userJson.getFirstName());
		userEntity.setLastName(userJson.getLastName());
		userEntity.setEmail(userJson.getEmail());
		userEntity.setPhone(userJson.getPhone());
		user.update(userEntity);


		if (userEntity != null) {
			return Response.status(200).entity("User updated").build();
		} else {
			return Response.status(404).entity("Bad response.").build();
		}
	}

}
