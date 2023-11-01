package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.DateTimeParserUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.addcommands.AddCompanyCommand;
import seedu.address.logic.commands.addcommands.AddInternshipCommand;
import seedu.address.logic.commands.addcommands.AddPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.company.Company;
import seedu.address.model.company.CompanyDescription;
import seedu.address.model.company.CompanyEmail;
import seedu.address.model.company.CompanyName;
import seedu.address.model.company.CompanyPhone;
import seedu.address.model.company.internship.Internship;
import seedu.address.model.company.internship.InternshipDescription;
import seedu.address.model.company.internship.InternshipInterviewDateTime;
import seedu.address.model.company.internship.InternshipName;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code personString} into an {@code Person}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Person parsePerson(String personString) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(personString,
                        PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (!argMultimap.arePrefixesPresent(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPersonCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new Person(name, phone, email, address, tagList);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code companyString} into an {@code Company}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Company parseCompany(String companyString) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(companyString,
                        PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_DESCRIPTION, PREFIX_TAG);

        if (!argMultimap.arePrefixesPresent(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_DESCRIPTION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCompanyCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_DESCRIPTION);
        CompanyName name = ParserUtil.parseCompanyName(argMultimap.getValue(PREFIX_NAME).get());
        CompanyPhone phone = ParserUtil.parseCompanyPhone(argMultimap.getValue(PREFIX_PHONE).get());
        CompanyEmail email = ParserUtil.parseCompanyEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        CompanyDescription description = ParserUtil.parseCompanyDescription(
                argMultimap.getValue(PREFIX_DESCRIPTION).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new Company(name, phone, email, description, tagList);
    }

    /**
     * Parses a {@code String companyName} into a {@code CompanyName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code companyName} is invalid.
     */
    public static CompanyName parseCompanyName(String companyName) throws ParseException {
        requireNonNull(companyName);
        String trimmedCompanyName = companyName.trim();
        if (!CompanyName.isValidName(trimmedCompanyName)) {
            throw new ParseException(CompanyName.MESSAGE_CONSTRAINTS);
        }
        return new CompanyName(trimmedCompanyName);
    }

    /**
     * Parses a {@code String internshipName} into a {@code InternshipName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code internshipName} is invalid.
     */
    public static InternshipName parseInternshipName(String internshipName) throws ParseException {
        requireNonNull(internshipName);
        String trimmedInternshipName = internshipName.trim();
        if (!InternshipName.isValidName(trimmedInternshipName)) {
            throw new ParseException(InternshipName.MESSAGE_CONSTRAINTS);
        }
        return new InternshipName(trimmedInternshipName);
    }

    /**
     * Parses a {@code String companyPhone} into a {@code CompanyPhone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code companyPhone} is invalid.
     */
    public static CompanyPhone parseCompanyPhone(String companyPhone) throws ParseException {
        requireNonNull(companyPhone);
        String trimmedCompanyPhone = companyPhone.trim();
        if (!Phone.isValidPhone(trimmedCompanyPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new CompanyPhone(trimmedCompanyPhone);
    }

    /**
     * Parses a {@code String companyDescription} into an {@code CompanyDescription}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code companyDescription} is invalid.
     */
    public static CompanyDescription parseCompanyDescription(String companyDescription) throws ParseException {
        requireNonNull(companyDescription);
        String trimmedDescription = companyDescription.trim();
        if (!CompanyDescription.isValidCompanyDescription(trimmedDescription)) {
            throw new ParseException(CompanyDescription.MESSAGE_CONSTRAINTS);
        }
        return new CompanyDescription(trimmedDescription);
    }

    /**
     * Parses a {@code String companyEmail} into an {@code CompanyEmail}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code companyEmail} is invalid.
     */
    public static CompanyEmail parseCompanyEmail(String companyEmail) throws ParseException {
        requireNonNull(companyEmail);
        String trimmedEmail = companyEmail.trim();
        if (!CompanyEmail.isValidEmail(trimmedEmail)) {
            throw new ParseException(CompanyEmail.MESSAGE_CONSTRAINTS);
        }
        return new CompanyEmail(trimmedEmail);
    }

    /**
     * Parses a {@code internshipString} into an {@code Internship}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Internship parseInternship(String internshipString) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(internshipString,
                        PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_SCHEDULED);

        if (!argMultimap.arePrefixesPresent(PREFIX_NAME, PREFIX_DESCRIPTION) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddInternshipCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_DESCRIPTION);
        InternshipName name = ParserUtil.parseInternshipName(argMultimap.getValue(PREFIX_NAME).get());
        InternshipDescription description = ParserUtil.parseInternshipDescription(
                argMultimap.getValue(PREFIX_DESCRIPTION).get());

        if (argMultimap.getValue(PREFIX_SCHEDULED).isPresent()) {
            InternshipInterviewDateTime interviewDateTime = ParserUtil.parseInternshipInterviewDateTime(
                    argMultimap.getValue(PREFIX_SCHEDULED).get());
            return new Internship(name, description, interviewDateTime);
        }

        return new Internship(name, description);
    }

    /**
     * Parses a {@code String internshipDescription} into an {@code InternshipDescription}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code internshipDescription} is invalid.
     */
    public static InternshipDescription parseInternshipDescription(String internshipDescription) throws ParseException {
        requireNonNull(internshipDescription);
        String trimmedDescription = internshipDescription.trim();
        if (!InternshipDescription.isValidDescription(trimmedDescription)) {
            throw new ParseException(CompanyDescription.MESSAGE_CONSTRAINTS);
        }
        return new InternshipDescription(trimmedDescription);
    }

    /**
     * Parses a {@code String interviewDateTime} into a {@code InternshipInterviewDateTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code interviewDateTime} is invalid.
     */
    public static InternshipInterviewDateTime parseInternshipInterviewDateTime(
            String interviewDateTime) throws ParseException {
        requireNonNull(interviewDateTime);
        String trimmedInterviewDateTime = interviewDateTime.trim();
        if (!InternshipInterviewDateTime.isValidInterviewDateTime(trimmedInterviewDateTime)) {
            throw new ParseException(InternshipInterviewDateTime.MESSAGE_CONSTRAINTS);
        }

        LocalDateTime dateTime = DateTimeParserUtil.parseStringToDateTime(trimmedInterviewDateTime);
        return new InternshipInterviewDateTime(dateTime);
    }
}
